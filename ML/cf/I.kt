package I

import kotlin.math.max
import kotlin.math.tanh

class Matrix(val rowCnt: Int, val columnCnt: Int, val matrix: List<List<Double>>) {

  fun mapElem(f: (Double) -> Double): Matrix {
    return Matrix(rowCnt, columnCnt, matrix.map { it.map(f) })
  }

  fun mapElemIndexed(f: (Int, Int, Double) -> Double): Matrix {
    return Matrix(rowCnt, columnCnt, matrix.mapIndexed { row, list ->
      list.mapIndexed { column, elem ->
        f(row, column, elem)
      }
    })
  }

  operator fun times(another: Matrix): Matrix = times(another, transpose = false, transposeAnother = false)

  fun times(another: Matrix, transpose: Boolean = false, transposeAnother: Boolean = false): Matrix {
    val m = if (!transpose) columnCnt else rowCnt
    val m1 = if (!transposeAnother) another.rowCnt else another.columnCnt
    assert(m == m1) {
      """
      Bad matrix multiplication dimension:
      A${if (transpose) "^T" else ""}[$rowCnt x $columnCnt] * B${if (transposeAnother) "^T" else ""}[${another.rowCnt} x ${another.columnCnt}]
    """.trimIndent()
    }
    val n = if (!transpose) rowCnt else columnCnt
    val k = if (!transposeAnother) another.columnCnt else another.rowCnt

    val resMatrix = mutableListOf<MutableList<Double>>()
    for (i in 0 until n) {
      resMatrix.add((0 until k).map { 0.0 }.toMutableList())
    }

    val anotherMatrix = another.matrix
    for (i in 0 until n) {
      for (j in 0 until k) {
        for (l in 0 until m) {
          val left = if (!transpose) matrix[i][l] else matrix[l][i]
          val right = if (!transposeAnother) anotherMatrix[l][j] else anotherMatrix[j][l]
          resMatrix[i][j] += left * right
        }
      }
    }
    return Matrix(n, k, resMatrix)
  }

  operator fun plus(another: Matrix): Matrix {
    val anotherMatrix = another.matrix
    return mapElemIndexed { row, column, value ->
      value + anotherMatrix[row][column]
    }
  }

  fun adamarMul(another: Matrix): Matrix {
    val anotherMatrix = another.matrix
    return mapElemIndexed { row, column, value ->
      value * anotherMatrix[row][column]
    }
  }

  fun print() {
    for (i in 0 until rowCnt) {
      for (j in 0 until columnCnt) {
        print("%.12f".format(matrix[i][j]))
        print(" ")
      }
      println()
    }
  }

  companion object {
    fun readMatrix(rowCnt: Int, columnCnt: Int): Matrix {
      val matrix = mutableListOf<List<Double>>()
      for (i in 0 until rowCnt) matrix.add(readDoubles())
      return Matrix(rowCnt, columnCnt, matrix)
    }

    fun getValueMatrix(rowCnt: Int, columnCnt: Int, value: Double): Matrix {
      val resMatrix = mutableListOf<MutableList<Double>>()
      for (i in 0 until rowCnt) {
        resMatrix.add((0 until columnCnt).map { value }.toMutableList())
      }
      return Matrix(rowCnt, columnCnt, resMatrix)
    }
  }
}

sealed class Node {

  private var functionCache: Matrix? = null
  protected lateinit var myDiff: Matrix
    private set

  protected abstract fun calcFunctionInner(): Matrix
  abstract fun pushDiff()

  fun calcFunction(): Matrix {
    return functionCache ?: calcFunctionInner().also { functionCache = it }
  }

  private val zeroMatrix by lazy {
    val functionMatrix = calcFunction()
    Matrix.getValueMatrix(functionMatrix.rowCnt, functionMatrix.columnCnt, 0.0)
  }
  val diff: Matrix
    get() {
      return if (!this::myDiff.isInitialized) zeroMatrix
      else myDiff
    }

  fun addDiff(diff: Matrix) {
    myDiff =
      if (!this::myDiff.isInitialized) diff
      else myDiff + diff
  }

  companion object {
    fun readNodes(n: Int, m: Int): List<Node> {
      val res = mutableListOf<Node>()
      for (i in 0 until n) {
        val data = readStrings()
        val args = data.drop(1).map { it.toInt() }
        res.add(
          when (data.first()) {
            "var" -> VarNode(args[0], args[1])
            "tnh" -> TnhNode(res[args[0] - 1])
            "rlu" -> RluNode(1.0 / args[0], res[args[1] - 1])
            "mul" -> MulNode(res[args[0] - 1], res[args[1] - 1])
            "sum" -> SumNode(args[0], args.drop(1).map { res[it - 1] })
            "had" -> HadNode(args[0], args.drop(1).map { res[it - 1] })
            else -> throw RuntimeException("Bad node type ${data.first()}")
          }
        )
      }

      for (i in 0 until m) {
        val inputNode = res[i] as VarNode
        inputNode.matrix = Matrix.readMatrix(inputNode.rowCnt, inputNode.columnCnt)
      }
      return res
    }
  }
}

data class VarNode(val rowCnt: Int, val columnCnt: Int) : Node() {
  lateinit var matrix: Matrix

  override fun calcFunctionInner(): Matrix = matrix

  override fun pushDiff() {
    // nothing
  }
}

data class TnhNode(val argNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix {
    return argNode.calcFunction().mapElem { tanh(it) }
  }

  override fun pushDiff() {
    argNode.addDiff(calcFunction().mapElemIndexed { row, column, tnh ->
      (1.0 - tnh * tnh) * myDiff.matrix[row][column]
    })
  }
}

data class RluNode(val alpha: Double, val argNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix {
    return argNode.calcFunction().mapElem { max(it, alpha * it) }
  }

  override fun pushDiff() {
    argNode.addDiff(argNode.calcFunction().mapElemIndexed { row, column, x ->
      myDiff.matrix[row][column] *
          if (x < 0.0) alpha
          else 1.0
    })
  }
}

data class MulNode(val leftNode: Node, val rightNode: Node) : Node() {
  override fun calcFunctionInner(): Matrix = leftNode.calcFunction() * rightNode.calcFunction()

  override fun pushDiff() {
    leftNode.addDiff(myDiff.times(rightNode.calcFunction(), transposeAnother = true))
    rightNode.addDiff(leftNode.calcFunction().times(myDiff, transpose = true))
  }
}

data class SumNode(val len: Int, val args: List<Node>) : Node() {
  override fun calcFunctionInner(): Matrix {
    return args.drop(1).fold(args.first().calcFunction()) { acc, cur -> acc + cur.calcFunction() }
  }

  override fun pushDiff() {
    args.forEach { it.addDiff(myDiff) }
  }
}

data class HadNode(val len: Int, val args: List<Node>) : Node() {
  override fun calcFunctionInner(): Matrix {
    return args.drop(1).fold(args.first().calcFunction()) { acc, cur -> acc.adamarMul(cur.calcFunction()) }
  }

  override fun pushDiff() {
    args.forEachIndexed { argInd, node ->
      val ex = args.first().calcFunction()
      node.addDiff((args).foldIndexed(Matrix.getValueMatrix(ex.rowCnt, ex.columnCnt, 1.0)) { ind, acc, cur ->
        if (ind == argInd) acc
        else acc.adamarMul(cur.calcFunction())
      }.adamarMul(myDiff))
    }
  }
}

private fun readLn() = readLine()!!.trim()
private fun readStrings() = readLn().split(" ")
private fun readInts() = readStrings().map { it.toInt() }
private fun readDoubles() = readStrings().map { it.toDouble() }

fun main() {
  val (n, m, k) = readInts()
  val nodes = Node.readNodes(n, m)
  nodes.takeLast(k).forEach {
    val resMatrix = it.calcFunction()
    resMatrix.print()
    println()
    it.addDiff(Matrix.readMatrix(resMatrix.rowCnt, resMatrix.columnCnt))
  }
  nodes.reversed().forEach { it.pushDiff() }
  nodes.take(m).forEach {
    it.diff.print()
    println()
  }
}
