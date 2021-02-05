package ifmo.mervap.bridge.graph

import ifmo.mervap.bridge.draw.DrawingApi
import java.io.InputStream
import java.util.*

class AdjacencyMatrixGraph(drawingApi: DrawingApi) : Graph(drawingApi) {

  private var verticesCnt: Int = -1
  private val matrix = mutableListOf<MutableList<Int>>()

  override fun readGraph(inputStream: InputStream) = with(Scanner(inputStream)) {
    verticesCnt = nextInt()
    matrix.clear()
    for (i in 0 until verticesCnt) {
      val row = mutableListOf<Int>()
      for (j in 0 until verticesCnt) {
        row.add(nextInt())
      }
      matrix.add(row)
    }
  }

  override fun drawGraph() {
    val points = calcCenterPolygon(verticesCnt)
    for (point in points) {
      drawingApi.drawCircle(point.x, point.y, NODE_RADIUS)
    }
    for (i in 0 until verticesCnt) {
      val point1 = points[i]
      for (j in 0 until verticesCnt) {
        val point2 = points[j]
        if (matrix[i][j] == 1) {
          drawingApi.drawLine(point1.x, point1.y, point2.x, point2.y)
        }
      }
    }
  }

  companion object {
    private const val NODE_RADIUS = 12.0
  }
}