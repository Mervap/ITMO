package G

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ln
import kotlin.properties.Delegates

typealias FeatureList = List<Int>
data class TrainingObject(val features: FeatureList, val label: Int)

data class Rule(val featureInd: Int, val border: Double) {
  fun isSatisfies(features: FeatureList): Boolean {
    return features[featureInd] < border
  }
}

interface NodeContent
data class InnerNodeWithRule(val rule: Rule, val left: Node, val right: Node) : NodeContent
data class LeafWithClass(val classInd: Int) : NodeContent

data class Node(val ind: Int, val nodeContent: NodeContent) {
  fun printTree() {
    if (nodeContent is InnerNodeWithRule) {
      val rule = nodeContent.rule
      println("Q ${rule.featureInd + 1} ${rule.border} ${nodeContent.left.ind} ${nodeContent.right.ind}")
      nodeContent.left.printTree()
      nodeContent.right.printTree()
    }
    else {
      println("C ${(nodeContent as LeafWithClass).classInd + 1}")
    }
  }
}

class TreeBuilder(private val classesCnt: Int, private val maxHeight: Int) {
  var treeSize = 0
    private set
  lateinit var root: Node
    private set
  private var inputFeaturesCnt by Delegates.notNull<Int>()

  fun buildTree(objs: List<TrainingObject>) {
    inputFeaturesCnt = objs.size
    root = buildTreeInner(objs, 0)
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
    for (ind in 0 until featuresCnt) {
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

private fun readLn() = readLine()!!
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }

fun main(args: Array<String>) {
  val (_, k, h) = readInts()
  val n = readInt()
  val trainingSet = (0 until n).map {
    val ints = readInts()
    TrainingObject(ints.dropLast(1), ints.last() - 1)
  }
  val res = TreeBuilder(k, h)
  res.buildTree(trainingSet)
  println(res.treeSize)
  res.root.printTree()
}