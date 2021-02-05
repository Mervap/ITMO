package com.itmo

import org.jetbrains.numkt.core.ExperimentalNumkt
import org.jetbrains.numkt.linspace
import java.io.File
import kotlin.math.exp
import kotlin.math.ln
import kotlin.properties.Delegates

typealias FeatureList = List<Double>
data class WeightedTrainingObject(var weight: Double, val features: FeatureList, val label: Int)

data class Rule(val featureInd: Int, val border: Double) {
  fun isSatisfies(features: FeatureList): Boolean {
    return features[featureInd] < border
  }
}

interface NodeContent {
  fun predict(features: FeatureList): Int
}

data class InnerNodeWithRule(val rule: Rule, val left: Node, val right: Node) : NodeContent {
  override fun predict(features: FeatureList): Int {
    return if (rule.isSatisfies(features)) left.predict(features)
    else right.predict(features)
  }
}

data class LeafWithClass(val classInd: Int) : NodeContent {
  override fun predict(features: FeatureList): Int = classInd
}

data class Node(val ind: Int, val nodeContent: NodeContent) {
  fun predict(features: FeatureList) = nodeContent.predict(features)
}

class DecisionStump {
  private lateinit var root: Node
  private var inputFeaturesCnt by Delegates.notNull<Int>()

  fun learnTree(objs: List<WeightedTrainingObject>): Double {
    inputFeaturesCnt = objs.size
    var bestWErr = 1e9
    var bestTree = Node(-1, LeafWithClass(0))
    for (ind in 0..1) {
      val sorted = objs.sortedBy { it.features[ind] }
      for (borderInd in 1 until objs.size) {
        val rule = Rule(ind, (sorted[borderInd].features[ind] + sorted[borderInd - 1].features[ind]) / 2.0)
        val lSubTree = objs.filter { rule.isSatisfies(it.features) }
        val rSubTree = objs.filter { !rule.isSatisfies(it.features) }
        val lClass = calcClass(lSubTree)
        val rClass = calcClass(rSubTree)
        val wErr = calcErr(lSubTree, lClass) + calcErr(rSubTree, rClass)
        if (wErr < bestWErr) {
          bestWErr = wErr
          bestTree = Node(
            0,
            InnerNodeWithRule(
              rule,
              Node(1, LeafWithClass(lClass)),
              Node(2, LeafWithClass(rClass))
            )
          )
        }
      }
    }

    if (bestTree.ind == -1) {
      throw RuntimeException("No find stump")
    }
    root = bestTree
    return bestWErr
  }

  fun predict(features: FeatureList): Int {
    return root.predict(features)
  }

  private fun calcErr(objs: List<WeightedTrainingObject>, predicted: Int) = objs.map { obj ->
    if (obj.label != predicted) obj.weight else 0.0
  }.sum()

  private fun calcClass(objs: List<WeightedTrainingObject>): Int {
    return countClasses(objs).run { indexOf(max()) }
  }

  private fun countClasses(objs: Collection<WeightedTrainingObject>): MutableList<Int> {
    val classesCnt = mutableListOf(0, 0)
    for (obj in objs) {
      ++classesCnt[obj.label]
    }
    return classesCnt
  }
}

class AdaBoost(private val objs: List<WeightedTrainingObject>) {
  private val stumps: MutableList<DecisionStump> = mutableListOf()
  private val alphas: MutableList<Double> = mutableListOf()
  private var stumpsCnt: Int = 0

  fun addTree() {
    ++stumpsCnt
    val tree = DecisionStump()
    val wErr = tree.learnTree(objs)
    stumps.add(tree)
    alphas.add(0.5 * ln((1.0 - wErr) / wErr))
    var sum = 0.0
    for (obj in objs) {
      val predicted = tree.predict(obj.features)
      val mul = if (predicted == obj.label) 1.0 else -1.0
      obj.weight = obj.weight * exp(-alphas.last() * mul)
      sum += obj.weight
    }
    for (obj in objs) {
      obj.weight /= sum
    }
  }

  fun predict(features: FeatureList): Int {
    var sum = 0.0
    stumps.forEachIndexed { ind, tree ->
      val predicted = tree.predict(features)
      sum += if (predicted == 1) alphas[ind] else -alphas[ind]
    }
    return if (sum > 0) 1 else 0
  }
}


private fun readLn() = readLine()!!
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readDoubles() = readStrings().map { it.toDouble() }

fun readDataSet(datasetFile: String): List<WeightedTrainingObject> {
  val file = File("data/$datasetFile")
  var content = file.readText().split("\\s+".toRegex()).filter { it.isNotBlank() }
  val (m, n) = content.take(2).map { it.toInt() }
  content = content.drop(2)
  return (0 until n).map {
    val ints = content.take(m + 1)
    content = content.drop(m + 1)
    WeightedTrainingObject(1.0 / n, ints.dropLast(1).map { it.toDouble() }, ints.last().toInt())
  }
}

fun calcAccuracy(adaBoost: AdaBoost, testSet: List<WeightedTrainingObject>): Double {
  var ok = 0
  for (testObj in testSet) {
    val predicted = adaBoost.predict(testObj.features)
    if (predicted == testObj.label) {
      ++ok
    }
  }
  return ok.toDouble() / testSet.size
}

@ExperimentalNumkt
fun calcGrid(adaBoost: AdaBoost, nCnt: Int, xMin: Double, xMax: Double, yMin: Double, yMax: Double): List<Int> {
  val res = mutableListOf<Int>()
  for (y in linspace<Double>(yMin, yMax, nCnt).toList()) {
    for (x in linspace<Double>(xMin, xMax, nCnt).toList()) {
      res.add(adaBoost.predict(listOf(x, y)))
    }
  }
  return res
}

@ExperimentalNumkt
fun main() {
  val datasetFile = readLn()
  val (maxTreeCnt) = readInts()
  val gridNum = readInts().toSet()
  val (nCnt, xMin, xMax, yMin, yMax) = readDoubles()
  val dataset = readDataSet(datasetFile)
  val adaBoost = AdaBoost(dataset)
  val grids = mutableListOf<List<Int>>()
  for (treeCnt in 1..maxTreeCnt) {
    adaBoost.addTree()
    val accuracy = calcAccuracy(adaBoost, dataset)
    print("$accuracy ")
    if (treeCnt in gridNum) {
      grids.add(calcGrid(adaBoost, nCnt.toInt(), xMin, xMax, yMin, yMax))
    }
  }
  grids.forEach { grid ->
    grid.forEach { print("$it ") }
  }
}