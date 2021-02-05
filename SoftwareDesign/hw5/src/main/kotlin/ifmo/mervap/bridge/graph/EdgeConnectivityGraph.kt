package ifmo.mervap.bridge.graph

import ifmo.mervap.bridge.draw.DrawingApi
import java.io.InputStream
import java.util.*

class EdgeConnectivityGraph(drawingApi: DrawingApi) : Graph(drawingApi) {

  private var verticesCnt: Int = -1
  private val edges = mutableMapOf<Int, MutableList<Int>>()

  override fun readGraph(inputStream: InputStream) = with(Scanner(inputStream)) {
    verticesCnt = nextInt()
    val m = nextInt()
    edges.clear()
    for (i in 0 until m) {
      val begin = nextInt() - 1
      val end = nextInt() - 1
      edges.putIfAbsent(begin, mutableListOf())
      edges.getValue(begin).add(end)
    }
  }

  override fun drawGraph() {
    val points = calcCenterPolygon(verticesCnt)
    for (point in points) {
      drawingApi.drawCircle(point.x, point.y, NODE_RADIUS)
    }
    for ((v1, edgs) in edges.entries) {
      val point1 = points[v1]
      for (v2 in edgs) {
        val point2 = points[v2]
        drawingApi.drawLine(point1.x, point1.y, point2.x, point2.y)
      }
    }
  }

  companion object {
    private const val NODE_RADIUS = 12.0
  }
}