package com.itmo

import java.io.File
import java.lang.Math.random
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.properties.Delegates

typealias FeatureList = List<Int>
data class TrainingObject(val features: FeatureList, val label: Int)

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

interface Classifier {
  fun predict(features: FeatureList): Int
}

class DecisionTree(private val classesCnt: Int, private val maxHeight: Int, private val forestTree: Boolean = false) : Classifier {
  var treeSize = 0
    private set
  private lateinit var root: Node
  private var inputFeaturesCnt by Delegates.notNull<Int>()

  fun learnTree(objs: List<TrainingObject>) {
    inputFeaturesCnt = objs.size
    root = buildTreeInner(objs, 0)
  }

  override fun predict(features: FeatureList): Int {
    return root.predict(features)
  }

  private fun countClasses(objs: Collection<TrainingObject>): MutableList<Int> {
    val classesCnt = ArrayList<Int>(classesCnt)
    for (cl in 0 until this.classesCnt) classesCnt.add(0)
    for (obj in objs) {
      ++classesCnt[obj.label]
    }
    return classesCnt
  }

  private fun giniImpurity(objs: ArrayDeque<TrainingObject>): Pair<MutableList<Int>, Double> {
    val classesCnt = countClasses(objs)
    var impurity = 0.0
    for (i in classesCnt.indices) {
      val p = classesCnt[i].toDouble()
      impurity += p * p
    }
    return classesCnt to impurity
  }

  private fun entropyImpurity(objs: ArrayDeque<TrainingObject>): Double {
    val classesCnt = countClasses(objs)
    var impurity = 0.0
    for (i in classesCnt.indices) {
      if (classesCnt[i] == 0) continue
      val p = classesCnt[i].toDouble() / objs.size
      impurity += -p * ln(p)
    }
    return impurity
  }

  private fun calcGain(lSubTree: ArrayDeque<TrainingObject>,
                       rSubTree: ArrayDeque<TrainingObject>,
                       lImpurity: Double,
                       rImpurity: Double): Double {
    return lSubTree.size * lImpurity + rSubTree.size * rImpurity
  }

  private fun giniSplit(ind: Int, sorted: List<TrainingObject>): Pair<Double, Rule> {
    var best = 1e9
    var bestRule = Rule(-1, 0.0)
    val leftSubtree = ArrayDeque(sorted.take(1))
    val rightSubtree = ArrayDeque(sorted.drop(1))
    var (lcnts, lImpuritySum) = giniImpurity(leftSubtree)
    var (rcnts, rImpuritySum) = giniImpurity(rightSubtree)
    for (borderInd in 1 until sorted.size) {
      val lImpurity = 1 - lImpuritySum / (leftSubtree.size * leftSubtree.size)
      val rImpurity = 1 - rImpuritySum / (rightSubtree.size * rightSubtree.size)
      val gain = calcGain(leftSubtree, rightSubtree, lImpurity, rImpurity)
      val movedObj = rightSubtree.pollFirst()
      leftSubtree.push(movedObj)
      val label = movedObj.label
      lImpuritySum -= lcnts[label] * lcnts[label]
      ++lcnts[label]
      lImpuritySum += lcnts[label] * lcnts[label]
      rImpuritySum -= rcnts[label] * rcnts[label]
      --rcnts[label]
      rImpuritySum += rcnts[label] * rcnts[label]

      if (gain < best) {
        best = gain
        bestRule = Rule(ind, (sorted[borderInd].features[ind] + sorted[borderInd - 1].features[ind]) / 2.0)
      }
    }
    return best to bestRule
  }

  private fun entropySplit(ind: Int, sorted: List<TrainingObject>): Pair<Double, Rule> {
    var bestGain = 1e9
    var bestRule = Rule(-1, 0.0)
    val leftSubtree = ArrayDeque(sorted.take(1))
    val rightSubtree = ArrayDeque(sorted.drop(1))
    for (borderInd in 1 until sorted.size) {
      val lImpurity = entropyImpurity(leftSubtree)
      val rImpurity = entropyImpurity(rightSubtree)
      val gain = calcGain(leftSubtree, rightSubtree, lImpurity, rImpurity)
      val movedObj = rightSubtree.pollFirst()
      leftSubtree.push(movedObj)
      if (gain < bestGain) {
        bestGain = gain
        bestRule = Rule(ind, (sorted[borderInd].features[ind] + sorted[borderInd - 1].features[ind]) / 2.0)
      }
    }
    return bestGain to bestRule
  }

  private fun createLeafNode(classesCnt: MutableList<Int>): Node {
    return Node(++treeSize, LeafWithClass(classesCnt.indexOf(classesCnt.max())))
  }

  private fun buildTreeInner(objs: List<TrainingObject>, curH: Int): Node {
    val classesCnt = countClasses(objs)
    if (curH >= maxHeight || classesCnt.any { it == objs.size }) {
      return createLeafNode(classesCnt)
    }
    var bestGain = 1e9
    var bestRule = Rule(-1, 0.0)
    val featuresCnt = objs[0].features.size
    val iterCnt = if (forestTree) sqrt(featuresCnt.toDouble()).toInt() + 1 else featuresCnt
    for (iter in 0 until iterCnt) {
      val ind = if (forestTree) (random() * featuresCnt).toInt() else iter
      val sorted = objs.sortedBy { it.features[ind] }
      if (sorted.first().features[ind] == sorted.last().features[ind]) {
        continue
      }
      val gain: Double
      val rule: Rule
      if (inputFeaturesCnt < 100) {
        val splitInfo = entropySplit(ind, sorted)
        gain = splitInfo.first
        rule = splitInfo.second
      }
      else {
        val splitInfo = giniSplit(ind, sorted)
        gain = splitInfo.first
        rule = splitInfo.second
      }
      if (gain < bestGain) {
        bestGain = gain
        bestRule = rule
      }
    }

    val lSubTree = objs.filter { bestRule.isSatisfies(it.features) }
    val rSubTree = objs.filter { !bestRule.isSatisfies(it.features) }
    if (lSubTree.isEmpty() || rSubTree.isEmpty()) {
      return createLeafNode(classesCnt)
    }
    return Node(
      ++treeSize,
      InnerNodeWithRule(
        bestRule,
        buildTreeInner(lSubTree, curH + 1),
        buildTreeInner(rSubTree, curH + 1)
      )
    )
  }
}

class RandomForest(private val classesCnt: Int): Classifier {
  private val trees: MutableList<DecisionTree> = mutableListOf()
  private var treeCnt: Int = 0

  fun addTree(objs: List<TrainingObject>) {
    ++treeCnt
    val tree = DecisionTree(classesCnt, objs.size + 1, true)
    tree.learnTree(objs)
    trees.add(tree)
  }

  override fun predict(features: FeatureList): Int {
    val predicted = ArrayList<Int>(classesCnt)
    for (cl in 0 until classesCnt) predicted.add(0)
    for (tree in trees) {
      ++predicted[tree.predict(features)]
    }
    return predicted.indexOf(predicted.maxOrNull())
  }
}


private fun readLn() = readLine()!!
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }

fun readDataSet(datasetNum: Int, isTest: Boolean): Pair<Int, List<TrainingObject>> {
  val suffix = if (isTest) "test" else "train"
  val file = File("data/%02d_$suffix.txt".format(datasetNum))
  var content = file.readText().split("\\s+".toRegex()).filter { it.isNotBlank() }.map { it.toInt() }
  val (m, k, n) = content.take(3)
  content = content.drop(3)
  val trainingSet = (0 until n).map {
    val ints = content.take(m + 1)
    content = content.drop(m + 1)
    TrainingObject(ints.dropLast(1), ints.last() - 1)
  }
  return k to trainingSet
}

fun calcAccuracy(classifier: Classifier, testSet: List<TrainingObject>): Double {
  var ok = 0
  for (testObj in testSet) {
    val predicted = classifier.predict(testObj.features)
    if (predicted == testObj.label) {
      ++ok
    }
  }
  return ok.toDouble() / testSet.size
}

fun main() {
  val mode = readLn()
  val accSrc = readLn()
  val (datasetNum, maxExtraParam) = readInts()
  val (k, trainingSet) = readDataSet(datasetNum, false)
  val (_, testSet) = readDataSet(datasetNum, true)
  if (mode == "tree") {
    for (h in 1..maxExtraParam) {
      val tree = DecisionTree(k, h)
      tree.learnTree(trainingSet)

      val accuracy =
        when (accSrc) {
          "test" -> calcAccuracy(tree, testSet).toString()
          "train" -> calcAccuracy(tree, trainingSet).toString()
          else -> calcAccuracy(tree, testSet).toString() + " " + calcAccuracy(tree, trainingSet).toString()
        }
      print("$accuracy ")
    }
  } else if (mode == "forest") {
    val forest = RandomForest(k)
    for (treeCnt in 1..maxExtraParam) {
      forest.addTree(trainingSet)

      val accuracy =
        when (accSrc) {
          "test" -> calcAccuracy(forest, testSet).toString()
          "train" -> calcAccuracy(forest, trainingSet).toString()
          else -> calcAccuracy(forest, testSet).toString() + " " + calcAccuracy(forest, trainingSet).toString()
        }
      print("$accuracy ")
    }
  }
}