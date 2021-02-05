package ifmo.mervap.bridge.graph

import ifmo.mervap.bridge.draw.DrawingApi
import ifmo.mervap.bridge.draw.Point
import ifmo.mervap.bridge.draw.PolygonUtil
import java.io.InputStream

abstract class Graph(protected val drawingApi: DrawingApi) {
  abstract fun readGraph(inputStream: InputStream)
  abstract fun drawGraph()

  protected fun calcCenterPolygon(verticesCnt: Int): List<Point> {
    val width = drawingApi.getDrawingAreaWidth()
    val height = drawingApi.getDrawingAreaHeight()
    val centerX = width / 2
    val centerY = height / 2
    return PolygonUtil.makeRegularPolygon(verticesCnt, centerX, centerY, 400.0)
  }

  companion object {
    fun instantiateGraph(graphType: Class<out Graph>, api: DrawingApi): Graph {
      return graphType.getConstructor(DrawingApi::class.java).newInstance(api)
    }
  }
}