package C

import java.util.*
import kotlin.math.*
import kotlin.properties.Delegates

typealias FeatureList = List<Int>
data class TrainingObject(val features: FeatureList, val label: Int)

enum class Metric {
  MANHATTAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0
      for (i in x.indices) {
        res += abs(x[i] - y[i])
      }
      return res.toDouble()
    }
  },
  EUCLIDEAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0
      for (i in x.indices) {
        val diff = x[i] - y[i]
        res += diff * diff
      }
      return sqrt(res.toDouble())
    }
  },
  CHEBYSHEV {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = -1
      for (i in x.indices) {
        res = max(res, abs(x[i] - y[i]))
      }
      return res.toDouble()
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

  open fun nadarayaWatson(trainingSet: List<TrainingObject>, newFeatures: FeatureList, metric: Metric, core: Core): Double {
    var fs = 0.0
    var sn = 0.0
    for (el in trainingSet) {
      val weight = core.compute(smoothing(metric.compute(el.features, newFeatures)))
      fs += el.label * weight
      sn += weight
    }
    if (sn < 1e-9) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return fs / sn
  }

  companion object {
    fun computeIfZero(trainingSet: List<TrainingObject>, newFeatures: FeatureList): Double {
      return trainingSet
        .filter { it.features == newFeatures }
        .takeIf { it.isNotEmpty() }
        ?.let { notEmptyTrainingSet ->
          notEmptyTrainingSet.map { it.label }.sum().toDouble() / notEmptyTrainingSet.size
        }
        ?: trainingSet.map { it.label }.sum().toDouble() / trainingSet.size
    }
  }
}

class Fixed(private val smoothingCoef: Int) : AlgorithmType() {
  override fun smoothing(u: Double): Double = u / smoothingCoef

  override fun nadarayaWatson(
    trainingSet: List<TrainingObject>,
    newFeatures: FeatureList,
    metric: Metric,
    core: Core
  ): Double {
    if (smoothingCoef == 0) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return super.nadarayaWatson(trainingSet, newFeatures, metric, core)
  }
}

class Variable(private val k: Int) : AlgorithmType() {
  private var smoothingCoef by Delegates.notNull<Double>()

  override fun smoothing(u: Double): Double = u / smoothingCoef

  override fun nadarayaWatson(trainingSet: List<TrainingObject>, newFeatures: FeatureList, metric: Metric, core: Core): Double {
    val sorted = trainingSet.sortedBy { metric.compute(it.features, newFeatures) }
    smoothingCoef = metric.compute(sorted[k].features, newFeatures)
    if (smoothingCoef < 1e-9) {
      return computeIfZero(trainingSet, newFeatures)
    }
    return super.nadarayaWatson(trainingSet, newFeatures, metric, core)
  }
}

fun main(args : Array<String>) = with(Scanner(System.`in`)) {
  val n = nextInt()
  val m = nextInt()
  val trainingSet = (0 until n).map { TrainingObject((0 until m).map { nextInt() }, nextInt()) }
  val newFeatures = (0 until m).map { nextInt() }
  nextLine()
  val metric = Metric.valueOf(nextLine().toUpperCase())
  val core = Core.valueOf(nextLine().toUpperCase())
  val type =
    if (nextLine() == "variable") Variable(nextInt())
    else Fixed(nextInt())
  print(type.nadarayaWatson(trainingSet, newFeatures, metric, core))
}