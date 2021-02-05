package ifmo.mervap.bridge.draw

import ifmo.mervap.bridge.graph.Graph

interface DrawingApi {
  fun getDrawingAreaWidth(): Double
  fun getDrawingAreaHeight(): Double
  fun drawCircle(centerX: Double, centerY: Double, radius: Double)
  fun drawLine(x1: Double, y1: Double, x2: Double, y2: Double)
  fun show(graphClass: Class<out Graph>, filepath: String)
}