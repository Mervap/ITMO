package K

import kotlin.math.exp
import kotlin.math.tanh

class Matrix(private val source: List<MutableList<Double>>) {
  val rowsCnt = source.size
  val columnsCnt = source.first().size

  fun mapElem(f: (Double) -> Double): Matrix {
    return Matrix(source.map { it.mapTo(ArrayList(columnsCnt), f) })
  }

  fun mapElemIndexed(f: (Int, Int, Double) -> Double): Matrix {
    return Matrix(source.mapIndexed { row, list ->
      list.mapIndexedTo(ArrayList(columnsCnt)) { column, elem ->
        f(row, column, elem)
      }
    })
  }

  operator fun times(another: Matrix): Matrix = times(another, transpose = false, transposeAnother = false)

  fun times(another: Matrix, transpose: Boolean = false, transposeAnother: Boolean = false): Matrix {
    val m = if (!transpose) columnsCnt else rowsCnt
    val m1 = if (!transposeAnother) another.rowsCnt else another.columnsCnt
    assert(m == m1) {
      """
      Bad matrix multiplication dimension:
      A${if (transpose) "^T" else ""}[$rowsCnt x $columnsCnt] * B${if (transposeAnother) "^T" else ""}[${another.rowsCnt} x ${another.columnsCnt}]
    """.trimIndent()
    }
    val n = if (!transpose) rowsCnt else columnsCnt
    val k = if (!transposeAnother) another.columnsCnt else another.rowsCnt

    val resMatrix = mutableListOf<MutableList<Double>>()
    for (i in 0 until n) {
      resMatrix.add((0 until k).map { 0.0 }.toMutableList())
    }

    for (i in 0 until n) {
      for (j in 0 until k) {
        for (l in 0 until m) {
          val left = if (!transpose) source[i][l] else source[l][i]
          val right = if (!transposeAnother) another[l][j] else another[j][l]
          resMatrix[i][j] += left * right
        }
      }
    }
    return Matrix(resMatrix)
  }

  operator fun get(ind: Int): MutableList<Double> = source[ind]

  operator fun plus(another: Matrix): Matrix {
    return mapElemIndexed { row, column, value ->
      value + another[row][column]
    }
  }

  fun adamarMul(another: Matrix): Matrix {
    return mapElemIndexed { row, column, value ->
      value * another[row][column]
    }
  }

  fun transposed(): Matrix {
    val res = mutableListOf<MutableList<Double>>()
    for (i in 0 until columnsCnt) {
      val row = mutableListOf<Double>()
      for (j in 0 until rowsCnt) {
        row.add(source[j][i])
      }
      res.add(row)
    }
    return Matrix(res)
  }

  fun print() {
    for (i in 0 until rowsCnt) {
      for (j in 0 until columnsCnt) {
        print("%.12f".format(source[i][j]))
        print(" ")
      }
      println()
    }
  }

  companion object {
    fun parseMatrix(rowsCnt: Int, columnsCnt: Int): Matrix {
      val matrix = mutableListOf<MutableList<Double>>()
      for (i in 0 until rowsCnt) matrix.add(readDoubles().toMutableList())
      return Matrix(matrix)
    }

    fun getValueMatrix(rowsCnt: Int, columnsCnt: Int, value: Double): Matrix {
      val resMatrix = mutableListOf<MutableList<Double>>()
      for (i in 0 until rowsCnt) {
        resMatrix.add((0 until columnsCnt).mapTo(ArrayList(columnsCnt)) { value })
      }
      return Matrix(resMatrix)
    }
  }
}

sealed class Node {

  private var functionCache: Matrix? = null
  private lateinit var myDeriv: Matrix

  protected abstract fun calcFunctionInner(): Matrix
  abstract fun pushDeriv()
  abstract val edges: List<Node>

  fun calcFunction(): Matrix {
    return functionCache ?: calcFunctionInner().also { functionCache = it }
  }

  private val zeroMatrix by lazy {
    calcFunction().let { Matrix.getValueMatrix(it.rowsCnt, it.columnsCnt, 0.0) }
  }

  val deriv: Matrix
    get() {
      return if (!this::myDeriv.isInitialized) zeroMatrix
      else myDeriv
    }

  fun addDeriv(deriv: Matrix) {
    myDeriv =
      if (!this::myDeriv.isInitialized) deriv
      else myDeriv + deriv
  }

  companion object {
    data class NetworkInfo(
      val wubNodes: List<Node>,
      val hm: Node,
      val cm: Node,
      val h0: Node,
      val c0: Node,
      val xs: List<Node>,
      val os: List<Node>
    )

    fun readNodes(): NetworkInfo {
      val inputLen = readInt()
      val matrixList = mutableListOf<VarNode>()
      for (i in 1..12) {
        matrixList.add(
          VarNode(
            if (i % 3 == 0) {
              Matrix.parseMatrix(1, inputLen).transposed()
            }
            else {
              Matrix.parseMatrix(inputLen, inputLen)
            }
          )
        )
      }
      val sequenceLen = readInt()
      val (wF, uF, bF, wI, uI, bI, wO, uO, bO, wC, uC, bC) = matrixList
      val shortMemoryNodes = mutableListOf<Node>(VarNode(Matrix.parseMatrix(1, inputLen).transposed()))
      val longMemoryNodes = mutableListOf<Node>(VarNode(Matrix.parseMatrix(1, inputLen).transposed()))
      val xs = mutableListOf<Node>()
      val outputNodes = mutableListOf<Node>()
      for (i in 1..sequenceLen) {
        val xi = VarNode(Matrix.parseMatrix(1, inputLen).transposed())
        val outputPrev = shortMemoryNodes.last()
        val statePrev = longMemoryNodes.last()
        val forgetGate = SigmNode(SumNode(MulNode(wF, xi), MulNode(uF, outputPrev), bF))
        val inputForgetGate = SigmNode(SumNode(MulNode(wI, xi), MulNode(uI, outputPrev), bI))
        val inputNewStateGate = TnhNode(SumNode(MulNode(wC, xi), MulNode(uC, outputPrev), bC))
        val inputGate = HadNode(inputForgetGate, inputNewStateGate)
        val longMemoryGate = SumNode(HadNode(forgetGate, statePrev), inputGate)
        val outputGate = SigmNode(SumNode(MulNode(wO, xi), MulNode(uO, outputPrev), bO))
        val shortMemoryGate = HadNode(outputGate, longMemoryGate)
        longMemoryNodes.add(longMemoryGate)
        shortMemoryNodes.add(shortMemoryGate)
        outputNodes.add(outputGate)
        xs.add(xi)
      }

      (outputNodes + longMemoryNodes.last() + shortMemoryNodes.last()).reversed().forEach {
        it.addDeriv(Matrix.parseMatrix(1, inputLen).transposed())
      }

      return NetworkInfo(
        matrixList,
        shortMemoryNodes.last(),
        longMemoryNodes.last(),
        shortMemoryNodes.first(),
        longMemoryNodes.first(),
        xs,
        outputNodes
      )
    }
  }
}

class VarNode(private val source: Matrix) : Node() {

  override fun calcFunctionInner(): Matrix = source

  override fun pushDeriv() {
    // nothing
  }

  override val edges: List<Node> = emptyList()
}

class TnhNode(private val argNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix {
    return argNode.calcFunction().mapElem { tanh(it) }
  }

  override fun pushDeriv() {
    argNode.addDeriv(calcFunction().mapElemIndexed { row, column, tnh ->
      (1.0 - tnh * tnh) * deriv[row][column]
    })
  }

  override val edges: List<Node> = listOf(argNode)
}

class SigmNode(val argNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix {
    return argNode.calcFunction().mapElem { 1.0 / (1.0 + exp(-it)) }
  }

  override fun pushDeriv() {
    argNode.addDeriv(calcFunction().mapElemIndexed { row, column, x ->
      deriv[row][column] * x * (1 - x)
    })
  }

  override val edges: List<Node> = listOf(argNode)
}

class MulNode(val leftNode: Node, val rightNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix = leftNode.calcFunction() * rightNode.calcFunction()

  override fun pushDeriv() {
    leftNode.addDeriv(deriv.times(rightNode.calcFunction(), transposeAnother = true))
    rightNode.addDeriv(leftNode.calcFunction().times(deriv, transpose = true))
  }

  override val edges: List<Node> = listOf(leftNode, rightNode)
}

class SumNode(val args: List<Node>) : Node() {
  constructor(vararg args: Node) : this(args.toList())

  override fun calcFunctionInner(): Matrix {
    return args.drop(1).fold(args.first().calcFunction()) { acc, cur -> acc + cur.calcFunction() }
  }

  override fun pushDeriv() {
    args.forEach { it.addDeriv(deriv) }
  }

  override val edges: List<Node> = args
}

class HadNode(val args: List<Node>) : Node() {
  constructor(vararg args: Node) : this(args.toList())

  override fun calcFunctionInner(): Matrix {
    return args.drop(1).fold(args.first().calcFunction()) { acc, cur -> acc.adamarMul(cur.calcFunction()) }
  }

  override fun pushDeriv() {
    args.forEachIndexed { argInd, node ->
      val ex = args.first().calcFunction()
      node.addDeriv((args).foldIndexed(Matrix.getValueMatrix(ex.rowsCnt, ex.columnsCnt, 1.0)) { ind, acc, cur ->
        if (ind == argInd) acc
        else acc.adamarMul(cur.calcFunction())
      }.adamarMul(deriv))
    }
  }

  override val edges: List<Node> = args
}

class TopologicalSortBuilder(private val startNodes: List<Node>) {
  private val was = mutableSetOf<Node>()
  private val topologicalSort = mutableListOf<Node>()

  fun build(): List<Node> {
    was.clear()
    topologicalSort.clear()
    for (node in startNodes) {
      if (!was.contains(node)) {
        dfs(node)
      }
    }

    return topologicalSort.reversed()
  }

  private fun dfs(node: Node) {
    was.add(node)
    for (edg in node.edges) {
      if (!was.contains(edg)) {
        dfs(edg)
      }
    }
    topologicalSort.add(node)
  }
}

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readDoubles() = readStrings().map { it.toDouble() }

private operator fun <T> List<T>.component6(): T = get(5)
private operator fun <T> List<T>.component7(): T = get(6)
private operator fun <T> List<T>.component8(): T = get(7)
private operator fun <T> List<T>.component9(): T = get(8)
private operator fun <T> List<T>.component10(): T = get(9)
private operator fun <T> List<T>.component11(): T = get(10)
private operator fun <T> List<T>.component12(): T = get(11)

fun main() {
  val (wubNodes, hm, cm, h0, c0, xs, os) = Node.readNodes()
  os.forEach { it.calcFunction().print() }
  hm.calcFunction().print()
  cm.calcFunction().print()
  TopologicalSortBuilder(listOf(hm)).build().forEach { it.pushDeriv() }
  xs.reversed().forEach { it.deriv.print() }
  h0.deriv.print()
  c0.deriv.print()
  wubNodes.forEach { it.deriv.print() }
}
