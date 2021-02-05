package com.itmo

import org.jetbrains.numkt.core.ExperimentalNumkt
import org.jetbrains.numkt.linspace
import java.io.File
import java.util.*
import kotlin.math.*
import kotlin.properties.Delegates

typealias FeatureList = List<Double>
typealias LabelList = List<Int>
typealias PredictedLabelList = List<Double>
data class TrainingObject(val features: FeatureList, val labels: LabelList)

enum class Metric {
  MANHATTAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0.0
      for (i in x.indices) {
        res += abs(x[i] - y[i])
      }
      return res
    }
  },
  EUCLIDEAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0.0
      for (i in x.indices) {
        val diff = x[i] - y[i]
        res += diff * diff
      }
      return sqrt(res)
    }
  },
  CHEBYSHEV {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = -1.0
      for (i in x.indices) {
        res = max(res, abs(x[i] - y[i]))
      }
      return res
    }
  };

  open fun compute(x: FeatureList, y: FeatureList): Double {
    if (x.size != y.size) throw RuntimeException("Bad feature list comparison")
    return 0.0
  }
}

enum class Core {
  UNIFORM {
    override fun compute(u: Double) = if (abs(u) < 1) 0.5 else 0.0
  },
  TRIANGULAR {
    override fun compute(u: Double) = abs(u).let { if (it < 1) 1 - it else 0.0 }
  },
  EPANECHNIKOV {
    override fun compute(u: Double) = if (abs(u) < 1) 0.75 * (1 - u.pow(2)) else 0.0
  },
  QUARTIC {
    override fun compute(u: Double) = if (abs(u) < 1) 15.0 / 16 * (1 - u.pow(2)).pow(2) else 0.0
  },
  TRIWEIGHT {
    override fun compute(u: Double) = if (abs(u) < 1) 35.0 / 32 * (1 - u.pow(2)).pow(3) else 0.0
  },
  TRICUBE {
    override fun compute(u: Double) = abs(u).let { if (it < 1) 70.0 / 81 * (1 - it.pow(3)).pow(3) else 0.0 }
  },
  GAUSSIAN {
    override fun compute(u: Double) = exp(-0.5 * u.pow(2)) / sqrt(2 * PI)
  },
  COSINE {
    override fun compute(u: Double) = if (abs(u) < 1) PI / 4 * cos(PI / 2 * u) else 0.0
  },
  LOGISTIC {
    override fun compute(u: Double) = 1 / (2 + exp(u) + exp(-u))
  },
  SIGMOID {
    override fun compute(u: Double) = 2 / (PI * (exp(u) + exp(-u)))
  };

  abstract fun compute(u: Double): Double
}

sealed class AlgorithmType {
  protected abstract fun smoothing(u: Double): Double
  abstract val presentation: String

  open fun nadarayaWatson(
    trainingSet: List<TrainingObject>,
    newFeatures: FeatureList,
    metric: Metric,
    core: Core
  ): PredictedLabelList {
    val fss = MutableList(trainingSet.first().labels.size) { 0.0 }
    var sn = 0.0
    for (el in trainingSet) {
      val weight = core.compute(smoothing(metric.compute(el.features, newFeatures)))
      el.labels.forEachIndexed { ind, label ->
        fss[ind] += label * weight
      }
      sn += weight
    }
    if (sn < 1e-9) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return fss.map { it / sn }
  }

  companion object {
    fun computeIfZero(trainingSet: List<TrainingObject>, newFeatures: FeatureList): PredictedLabelList {
      return trainingSet
        .filter { it.features == newFeatures }
        .takeIf { it.isNotEmpty() }
        ?.let { notEmptyTrainingSet ->
          notEmptyTrainingSet.first().labels.indices.map { ind ->
            notEmptyTrainingSet.map { it.labels[ind] }.sum().toDouble() / notEmptyTrainingSet.size
          }
        }
        ?: trainingSet.first().labels.indices.map { ind ->
          trainingSet.map { it.labels[ind] }.sum().toDouble() / trainingSet.size
        }
    }
  }
}

class Fixed(private val smoothingCoef: Double) : AlgorithmType() {
  override fun smoothing(u: Double): Double = u / smoothingCoef

  override val presentation: String
    get() = "Fixed with $smoothingCoef coef"

  override fun nadarayaWatson(
    trainingSet: List<TrainingObject>,
    newFeatures: FeatureList,
    metric: Metric,
    core: Core
  ): PredictedLabelList {
    if (smoothingCoef - 0.0 < 1e-9) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return super.nadarayaWatson(trainingSet, newFeatures, metric, core)
  }
}

class Variable(private val k: Int) : AlgorithmType() {
  private var smoothingCoef by Delegates.notNull<Double>()

  override fun smoothing(u: Double): Double = u / smoothingCoef

  override val presentation: String
    get() = "Variable with $k neighbors"

  override fun nadarayaWatson(
    trainingSet: List<TrainingObject>,
    newFeatures: FeatureList,
    metric: Metric,
    core: Core
  ): PredictedLabelList {
    val sorted = trainingSet.sortedBy { metric.compute(it.features, newFeatures) }
    smoothingCoef = metric.compute(sorted[k].features, newFeatures)
    if (smoothingCoef < 1e-9) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return super.nadarayaWatson(trainingSet, newFeatures, metric, core)
  }
}

fun createZeroArray(size: Int): Array<IntArray> {
  return (0 until size).map { IntArray(size) }.toTypedArray()
}

data class TestData(val metric: Metric, val core: Core, val type: AlgorithmType, val isOneHot: Boolean)

fun leaveOneOut(testData: TestData, trainingSet: List<TrainingObject>): FMeasure {
  val classesCnt = trainingSet.map { it.labels.first() }.maxOrNull()!! + 1
  val confusionMatrix = createZeroArray(classesCnt)
  val realTrainingSet =
    if (testData.isOneHot) trainingSet.map { obj ->
      TrainingObject(obj.features, List(classesCnt) { if (obj.labels.first() == it) 1 else 0 })
    }
    else trainingSet
  for (obj in realTrainingSet) {
    val predicted = testData.type.nadarayaWatson(realTrainingSet.filter { it != obj }, obj.features, testData.metric, testData.core)
    val expLabel: Int
    val realLabel: Int
    if (testData.isOneHot) {
      expLabel = obj.labels.indexOf(1)
      var labelProb = 0.0
      var bestLabel = 0
      predicted.forEachIndexed { ind, label ->
        if (label >= labelProb) {
          labelProb = label
          bestLabel = ind
        }
      }
      realLabel = bestLabel
    }
    else {
      expLabel = obj.labels.first()
      realLabel = predicted.first().roundToInt()
    }
    ++confusionMatrix[expLabel][realLabel]
  }
  return calcFMeasure(confusionMatrix)
}

@ExperimentalNumkt
fun allSequence(isOneHot: Boolean, fixedCnt: Int, variableCnt: Int) = sequence {
  for (metric in Metric.values()) {
    for (core in Core.values()) {
      for (fx in linspace<Double>(0.001, 5, fixedCnt).toList()) {
        yield(TestData(metric, core, Fixed(fx), isOneHot))
      }
      for (num in linspace<Int>(1, 100, variableCnt).toList()) {
        yield(TestData(metric, core, Variable(num), isOneHot))
      }
    }
  }
}

@ExperimentalNumkt
fun main() = with(Scanner(System.`in`)) {
  val objCnt = nextInt()
  val featuresCnt = nextInt()
  val trainingSet = (0 until objCnt).map {
    TrainingObject((0 until featuresCnt).map { nextDouble() }, listOf(nextDouble().toInt()))
  }

  nextLine()
  val isOneHot = nextLine().toBoolean()
  when (nextLine()) {
    "check_all" -> {
      var bestMicro: Pair<Double, TestData?> = -1.0 to null
      var bestMacro: Pair<Double, TestData?> = -1.0 to null
      var cnt = 0
      val fixedCnt = 40
      val variableCnt = 60
      val all = Metric.values().size * Core.values().size * (fixedCnt + variableCnt)
      for (data in allSequence(isOneHot, fixedCnt, variableCnt)) {
        val mFeasure = leaveOneOut(data, trainingSet)
        if (mFeasure.micro > bestMicro.first) {
          bestMicro = mFeasure.micro to data
//          File("/home/Valeriy.Teplyakov/ITMO/ML/Labs/knn/micro").writeText(mFeasure.micro.toString())
        }
        if (mFeasure.macro > bestMacro.first) {
          bestMacro = mFeasure.macro to data
//          File("/home/Valeriy.Teplyakov/ITMO/ML/Labs/knn/macro").writeText(mFeasure.micro.toString())
        }
        ++cnt
        File("/home/Valeriy.Teplyakov/ITMO/ML/Labs/knn/cnt").writeText("$cnt/$all")
      }

      println("""
        Micro
        ${bestMicro.first}
        ${bestMicro.second!!.metric.name}
        ${bestMicro.second!!.core.name}
        ${bestMicro.second!!.type.presentation}
        
        Macro
        ${bestMacro.first}
        ${bestMacro.second!!.metric.name}
        ${bestMacro.second!!.core.name}
        ${bestMacro.second!!.type.presentation}
      """.trimIndent())
    }
    "check_by_type" -> {
      val metric = nextLine().toUpperCase()
      val core = nextLine().toUpperCase()
      val type = nextLine().toUpperCase()

      val fixedCnt = if (type == "FIXED") 100 else 0
      val variableCnt = if (type == "VARIABLE") 100 else 0
      for (data in allSequence(isOneHot, fixedCnt, variableCnt)) {
        if (data.metric.name == metric && data.core.name == core) {
          val fMeasure = leaveOneOut(data, trainingSet)
          print(fMeasure.micro)
          print(" ")
          println(fMeasure.macro)
        }
      }
    }
    else -> {
      throw RuntimeException("Bad alg type")
    }
  }
}