package J

import kotlin.math.max

data class SquareMatrix(private val source: List<MutableList<Double>>) {
  val dim = source.size

  init {
    require(source.size == source.first().size) { "Matrix source should have square dimension" }
  }

  fun mapElem(f: (Double) -> Double): SquareMatrix {
    return SquareMatrix(source.map { it.mapTo(ArrayList(dim), f) })
  }

  fun mapElemIndexed(f: (Int, Int, Double) -> Double): SquareMatrix {
    return SquareMatrix(source.mapIndexed { row, list ->
      list.mapIndexedTo(ArrayList(dim)) { column, elem ->
        f(row, column, elem)
      }
    })
  }

  fun forEach(f: (MutableList<Double>) -> Unit) = source.forEach(f)

  operator fun times(another: SquareMatrix): SquareMatrix = times(another, transpose = false, transposeAnother = false)

  fun times(another: SquareMatrix, transpose: Boolean = false, transposeAnother: Boolean = false): SquareMatrix {
    assert(dim == another.dim) {
      """
      Bad matrix multiplication dimension:
      A${if (transpose) "^T" else ""}[$dim x $dim] * B${if (transposeAnother) "^T" else ""}[${another.dim} x ${another.dim}]
    """.trimIndent()
    }

    val resMatrix = getValueMatrix(dim, 0.0)
    for (i in 0 until dim) {
      for (j in 0 until dim) {
        for (l in 0 until dim) {
          val left = if (!transpose) source[i][l] else source[l][i]
          val right = if (!transposeAnother) another[l][j] else another[j][l]
          resMatrix[i][j] += left * right
        }
      }
    }
    return resMatrix
  }

  operator fun get(ind: Int): MutableList<Double> = source[ind]

  operator fun plus(another: SquareMatrix): SquareMatrix {
    val anotherMatrix = another.source
    return mapElemIndexed { row, column, value ->
      value + anotherMatrix[row][column]
    }
  }

  fun print() {
    for (i in 0 until dim) {
      for (j in 0 until dim) {
        print("%.12f".format(source[i][j]))
        print(" ")
      }
      println()
    }
  }

  companion object {
    fun parseMatrix(dim: Int, source: List<Double>): SquareMatrix {
      val matrix = mutableListOf<MutableList<Double>>()
      for (i in 0 until dim) matrix.add(source.subList(dim * i, dim * (i + 1)).toMutableList())
      return SquareMatrix(matrix)
    }

    fun getValueMatrix(dim: Int, value: Double): SquareMatrix {
      val resMatrix = mutableListOf<MutableList<Double>>()
      for (i in 0 until dim) {
        resMatrix.add((0 until dim).mapTo(ArrayList(dim)) { value })
      }
      return SquareMatrix(resMatrix)
    }
  }
}

data class Matrix3D(private val layers: List<SquareMatrix>) {
  val depth = layers.size
  val dim = layers.first().dim
  val indices = layers.indices

  operator fun get(ind: Int) = layers[ind]

  operator fun plus(other: Matrix3D): Matrix3D {
    return Matrix3D(layers.mapIndexed { ind, squareMatrix -> squareMatrix + other[ind] })
  }

  fun map(f: (SquareMatrix) -> SquareMatrix) = Matrix3D(layers.map(f))

  fun mapIndexed(f: (Int, SquareMatrix) -> SquareMatrix) = Matrix3D(layers.mapIndexed(f))

  fun forEach(f: (SquareMatrix) -> Unit) = layers.forEach(f)

  companion object {
    fun parseMatrix3D(depth: Int, dim: Int, source: List<Double>): Matrix3D {
      val matrix = mutableListOf<SquareMatrix>()
      val dimSqr = dim * dim
      for (i in 0 until depth) matrix.add(SquareMatrix.parseMatrix(dim, source.subList(dimSqr * i, dimSqr * (i + 1))))
      return Matrix3D(matrix)
    }

    fun getValueMatrix3D(depth: Int, dim: Int, value: Double): Matrix3D {
      val resMatrix3D = mutableListOf<SquareMatrix>()
      for (i in 0 until depth) {
        resMatrix3D.add(SquareMatrix.getValueMatrix(dim, value))
      }
      return Matrix3D(resMatrix3D)
    }
  }
}

interface NodeWithMutableParams {
  fun printParamDeriv()
}

sealed class Node {

  private var functionCache: Matrix3D? = null
  private lateinit var myDeriv: Matrix3D

  protected abstract fun calcFunctionInner(): Matrix3D
  abstract fun pushDeriv()

  fun calcFunction(): Matrix3D {
    return functionCache ?: calcFunctionInner().also { functionCache = it }
  }

  private val zeroMatrix by lazy {
    calcFunction().let { Matrix3D.getValueMatrix3D(it.depth, it.dim, 0.0) }
  }
  val deriv: Matrix3D
    get() {
      return if (!this::myDeriv.isInitialized) zeroMatrix
      else myDeriv
    }

  fun addDeriv(deriv: Matrix3D) {
    myDeriv =
      if (!this::myDeriv.isInitialized) deriv
      else myDeriv + deriv
  }

  companion object {
    fun readNodes(): List<Node> {
      val inputMatrixInfo = readStrings()
      val (dim, depth) = inputMatrixInfo.take(2).map { it.toInt() }
      val inputMatrix3D = Matrix3D.parseMatrix3D(depth, dim, inputMatrixInfo.drop(2).map { it.toDouble() })
      val res = mutableListOf<Node>(VarNode(inputMatrix3D))
      val l = readInt()
      for (i in 0 until l) {
        val data = readStrings()
        val args = data.drop(1)
        res.add(
          when (data.first()) {
            "relu" -> ReluNode(1.0 / args[0].toInt(), res.last())
            "pool" -> PoolNode(args[0].toInt(), res.last())
            "bias" -> BiasNode(args.map { it.toDouble() }, res.last())
            else -> {
              val (h, k, s, p) = args.take(4).map { it.toInt() }
              val kernel = args.drop(4).map { it.toDouble() }
              when (data.first()) {
                "cnvm" -> CnvmNode(h, k, s, p, kernel, res.last())
                "cnve" -> CnveNode(h, k, s, p, kernel, res.last())
                "cnvc" -> CnvcNode(h, k, s, p, kernel, res.last())
                else -> throw RuntimeException("Bad node type ${data.first()}")
              }
            }
          }
        )
      }

      return res
    }
  }
}

data class VarNode(val layers: Matrix3D) : Node() {
  override fun calcFunctionInner(): Matrix3D = layers

  override fun pushDeriv() {
    // Nothing
  }
}

data class ReluNode(val alpha: Double, val prev: Node) : Node() {
  override fun calcFunctionInner(): Matrix3D {
    return prev.calcFunction().map { layer -> layer.mapElem { max(it, alpha * it) } }
  }

  override fun pushDeriv() {
    prev.addDeriv(prev.calcFunction().mapIndexed { layerInd, layer ->
      val layerDeriv = deriv[layerInd]
      layer.mapElemIndexed { row, column, x ->
        layerDeriv[row][column] *
            if (x < 0.0) alpha
            else 1.0
      }
    })
  }
}

data class PoolNode(val sub: Int, val prev: Node) : Node() {
  override fun calcFunctionInner(): Matrix3D {
    return prev.calcFunction().map { layer ->
      val resMatrix = mutableListOf<MutableList<Double>>()
      val newDim = layer.dim / sub
      for (iIter in 0 until newDim) {
        val row = mutableListOf<Double>()
        for (jIter in 0 until newDim) {
          var mxValue: Double? = null
          val iSt = iIter * sub
          val jSt = jIter * sub
          for (i in 0 until sub) {
            for (j in 0 until sub) {
              val cellVal = layer[iSt + i][jSt + j]
              mxValue = max(mxValue ?: cellVal, cellVal)
            }
          }
          row.add(mxValue!!)
        }
        resMatrix.add(row)
      }
      SquareMatrix(resMatrix)
    }
  }

  override fun pushDeriv() {
    val layers = calcFunction()
    val deriv = prev.calcFunction().mapIndexed { layerInd, prevLayer ->
      val prevLayerDeriv = SquareMatrix.getValueMatrix(prevLayer.dim, 0.0)
      val layerDeriv = deriv[layerInd]
      val layer = layers[layerInd]
      val dim = layer.dim
      for (iIter in 0 until dim) {
        for (jIter in 0 until dim) {
          val mxValue = layer[iIter][jIter]
          val iSt = iIter * sub
          val jSt = jIter * sub
          for (i in 0 until sub) {
            for (j in 0 until sub) {
              val cellVal = prevLayer[iSt + i][jSt + j]
              if (cellVal == mxValue) {
                prevLayerDeriv[iSt + i][jSt + j] = layerDeriv[iIter][jIter]
                layerDeriv[iIter][jIter]
              }
            }
          }
        }
      }
      prevLayerDeriv
    }
    prev.addDeriv(deriv)
  }
}

data class BiasNode(val b: List<Double>, val prev: Node) : Node(), NodeWithMutableParams {
  override fun calcFunctionInner(): Matrix3D {
    return prev.calcFunction().mapIndexed { layerInd, layer ->
      val add = b[layerInd]
      layer.mapElem { it + add }
    }
  }

  override fun pushDeriv() {
    prev.addDeriv(deriv)
  }

  override fun printParamDeriv() {
    deriv.forEach { layer ->
      var sum = 0.0
      layer.forEach { row ->
        row.forEach { sum += it }
      }
      print(sum)
      print(" ")
    }
    println()
  }
}

abstract class CnvxNode(
  private val h: Int,
  private val k: Int,
  private val s: Int,
  protected val p: Int,
  unparsedKernel: List<Double>,
  private val prev: Node
) : Node(), NodeWithMutableParams {

  private val kernelDeriv by lazy {
    val depth = kernel.first().depth
    val dim = kernel.first().dim
    kernel.map { Matrix3D.getValueMatrix3D(depth, dim, 0.0) }
  }

  protected abstract fun fill(dim: Int, matrix: SquareMatrix)

  private val kernel by lazy {
    val depth = prev.calcFunction().depth
    val result = mutableListOf<Matrix3D>()
    val matrixElemCnt = depth * k * k
    for (layerInd in 0 until h) {
      result.add(
        Matrix3D.parseMatrix3D(
          depth,
          k,
          unparsedKernel.subList(matrixElemCnt * layerInd, matrixElemCnt * (layerInd + 1))
        )
      )
    }
    result
  }

  private fun getPaddedMatrix(layer: SquareMatrix): SquareMatrix {
    val dim = layer.dim
    val padded = mutableListOf<MutableList<Double>>()
    for (i in 0 until p) {
      val row = mutableListOf<Double>()
      for (j in 0 until dim + 2 * p) {
        row.add(0.0)
      }
      padded.add(row)
    }
    for (i in 0 until dim) {
      val row = mutableListOf<Double>()
      for (j in 0 until p) {
        row.add(0.0)
      }
      for (j in 0 until dim) {
        row.add(layer[i][j])
      }
      for (j in 0 until p) {
        row.add(0.0)
      }
      padded.add(row)
    }
    for (i in 0 until p) {
      val row = mutableListOf<Double>()
      for (j in 0 until dim + 2 * p) {
        row.add(0.0)
      }
      padded.add(row)
    }
    val matrix = SquareMatrix(padded)
    fill(dim, matrix)
    return matrix
  }

  private var paddedLayersCache: Matrix3D? = null

  private val paddedLayers: Matrix3D
    get() {
      return if (paddedLayersCache != null) paddedLayersCache!!
      else prev.calcFunction().map { getPaddedMatrix(it) }.also { paddedLayersCache = it }
    }

  override fun calcFunctionInner(): Matrix3D {
    val paddedLayers = paddedLayers
    val oldLayersDepth = paddedLayers.depth
    val dim = paddedLayers.dim
    val newDim = (dim - k) / s + 1
    val newLayers = mutableListOf<SquareMatrix>()
    for (layerInd in 0 until h) {
      val newLayer = mutableListOf<MutableList<Double>>()
      for (iIter in 0 until newDim) {
        val row = mutableListOf<Double>()
        for (jIter in 0 until newDim) {
          var cellValue = 0.0
          val iSt = iIter * s
          val jSt = jIter * s
          for (oldLayerInd in 0 until oldLayersDepth) {
            for (i in 0 until k) {
              for (j in 0 until k) {
                cellValue += paddedLayers[oldLayerInd][iSt + i][jSt + j] * kernel[layerInd][oldLayerInd][i][j]
              }
            }
          }
          row.add(cellValue)
        }
        newLayer.add(row)
      }
      newLayers.add(SquareMatrix(newLayer))
    }
    return Matrix3D(newLayers)
  }

  override fun pushDeriv() {
    val paddedLayers = paddedLayers
    val oldLayers = prev.calcFunction()
    val oldDim = oldLayers.dim
    val paddedDim = oldDim + 2 * p
    val pushDeriv = Matrix3D.getValueMatrix3D(oldLayers.depth, paddedDim, 0.0)

    val newDim = deriv.dim
    for (layerInd in 0 until h) {
      for (iIter in 0 until newDim) {
        for (jIter in 0 until newDim) {
          val iSt = iIter * s
          val jSt = jIter * s
          for (oldLayerInd in oldLayers.indices) {
            for (i in 0 until k) {
              for (j in 0 until k) {
                pushDeriv[oldLayerInd][iSt + i][jSt + j] += deriv[layerInd][iIter][jIter] * kernel[layerInd][oldLayerInd][i][j]
                kernelDeriv[layerInd][oldLayerInd][i][j] += deriv[layerInd][iIter][jIter] * paddedLayers[oldLayerInd][iSt + i][jSt + j]
              }
            }
          }
        }
      }
    }

    prev.addDeriv(pushDeriv.map { layer ->
      addPaddedDeriv(layer)

      val derivLayer = mutableListOf<MutableList<Double>>()
      for (i in 0 until oldDim) {
        val row = mutableListOf<Double>()
        for (j in 0 until oldDim) {
          row.add(layer[i + p][j + p])
        }
        derivLayer.add(row)
      }

      SquareMatrix(derivLayer)
    })
  }

  override fun printParamDeriv() {
    kernelDeriv.forEach { old -> old.forEach { new -> new.forEach { row -> row.forEach { print("$it ") } } } }
    println()
  }

  protected abstract fun addPaddedDeriv(derivLayer: SquareMatrix)
}

class CnvmNode(h: Int, k: Int, s: Int, p: Int, unparsedKernel: List<Double>, prev: Node) :
  CnvxNode(h, k, s, p, unparsedKernel, prev) {
  override fun fill(dim: Int, matrix: SquareMatrix) {
    for (i in 0 until p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[2 * p - i][2 * p - j]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[2 * p - i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[2 * p - i][2 * (dim + p - 1) - j]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[i][2 * p - j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[i][2 * (dim + p - 1) - j]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[2 * (dim + p - 1) - i][2 * p - j]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[2 * (dim + p - 1) - i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[2 * (dim + p - 1) - i][2 * (dim + p - 1) - j]
      }
    }
  }

  override fun addPaddedDeriv(derivLayer: SquareMatrix) {
    val dim = derivLayer.dim - 2 * p
    for (i in 0 until p) {
      for (j in 0 until p) {
        derivLayer[2 * p - i][2 * p - j] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[2 * p - i][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[2 * p - i][2 * (dim + p - 1) - j] += derivLayer[i][j]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        derivLayer[i][2 * p - j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[i][2 * (dim + p - 1) - j] += derivLayer[i][j]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        derivLayer[2 * (dim + p - 1) - i][2 * p - j] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[2 * (dim + p - 1) - i][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[2 * (dim + p - 1) - i][2 * (dim + p - 1) - j] += derivLayer[i][j]
      }
    }
  }
}

class CnveNode(h: Int, k: Int, s: Int, p: Int, unparsedKernel: List<Double>, prev: Node) :
  CnvxNode(h, k, s, p, unparsedKernel, prev) {
  override fun fill(dim: Int, matrix: SquareMatrix) {
    for (i in 0 until p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[p][p]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[p][j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[p][p + dim - 1]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[i][p]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[i][p + dim - 1]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[dim + p - 1][p]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[dim + p - 1][j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[dim + p - 1][dim + p - 1]
      }
    }
  }

  override fun addPaddedDeriv(derivLayer: SquareMatrix) {
    val dim = derivLayer.dim - 2 * p
    for (i in 0 until p) {
      for (j in 0 until p) {
        derivLayer[p][p] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[p][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[p][p + dim - 1] += derivLayer[i][j]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        derivLayer[i][p] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[i][p + dim - 1] += derivLayer[i][j]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        derivLayer[dim + p - 1][p] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[dim + p - 1][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[dim + p - 1][dim + p - 1] += derivLayer[i][j]
      }
    }
  }
}

class CnvcNode(h: Int, k: Int, s: Int, p: Int, unparsedKernel: List<Double>, prev: Node) :
  CnvxNode(h, k, s, p, unparsedKernel, prev) {
  override fun fill(dim: Int, matrix: SquareMatrix) {
    for (i in 0 until p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[dim + i][dim + j]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[dim + i][j]
      }
      for (j in dim + p until dim + 2 * p) {

        matrix[i][j] = matrix[dim + i][j - dim]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[i][dim + j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[i][j - dim]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        matrix[i][j] = matrix[i - dim][dim + j]
      }
      for (j in p until dim + p) {
        matrix[i][j] = matrix[i - dim][j]
      }
      for (j in dim + p until dim + 2 * p) {
        matrix[i][j] = matrix[i - dim][j - dim]
      }
    }
  }

  override fun addPaddedDeriv(derivLayer: SquareMatrix) {
    val dim = derivLayer.dim - 2 * p
    for (i in 0 until p) {
      for (j in 0 until p) {
        derivLayer[dim + i][dim + j] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[dim + i][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[dim + i][j - dim] += derivLayer[i][j]
      }
    }
    for (i in p until dim + p) {
      for (j in 0 until p) {
        derivLayer[i][dim + j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[i][j - dim] += derivLayer[i][j]
      }
    }
    for (i in dim + p until dim + 2 * p) {
      for (j in 0 until p) {
        derivLayer[i - dim][dim + j] += derivLayer[i][j]
      }
      for (j in p until dim + p) {
        derivLayer[i - dim][j] += derivLayer[i][j]
      }
      for (j in dim + p until dim + 2 * p) {
        derivLayer[i - dim][j - dim] += derivLayer[i][j]
      }
    }
  }
}

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readDoubles() = readStrings().map { it.toDouble() }

fun main() {
  val nodes = Node.readNodes()
  nodes.last().let { outputNode ->
    val layers = outputNode.calcFunction()
    layers.forEach {
      it.print()
      println()
    }
    outputNode.addDeriv(Matrix3D.parseMatrix3D(layers.depth, layers.dim, readDoubles()))
  }
  nodes.reversed().forEach { it.pushDeriv() }
  nodes.first().deriv.forEach {
    it.print()
    println()
  }
  nodes.forEach {
    if (it is NodeWithMutableParams) {
      it.printParamDeriv()
    }
  }
}
