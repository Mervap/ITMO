package ifmo.mervap.bridge.draw

import ifmo.mervap.bridge.graph.Graph
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.io.File
import kotlin.system.exitProcess

class AwtDrawingApi : Frame(), DrawingApi {

  private val circles = mutableListOf<Ellipse2D>()
  private val lines = mutableListOf<Line2D>()

  override fun paint(g: Graphics) {
    val ga = g as Graphics2D
    circles.forEach { ga.fill(it) }
    lines.forEach { ga.draw(it) }
  }

  override fun getDrawingAreaWidth(): Double = WIDTH.toDouble()

  override fun getDrawingAreaHeight(): Double = HEIGHT.toDouble()

  override fun drawCircle(centerX: Double, centerY: Double, radius: Double) {
    circles.add(Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2))
  }

  override fun drawLine(x1: Double, y1: Double, x2: Double, y2: Double) {
    lines.add(Line2D.Double(x1, y1, x2, y2))
  }

  override fun show(graphClass: Class<out Graph>, filepath: String) {
    title = "AWT graph ($filepath)"
    val graph = Graph.instantiateGraph(graphClass, this)
    graph.readGraph(File(filepath).inputStream())
    graph.drawGraph()
    addWindowListener(object : WindowAdapter() {
      override fun windowClosing(we: WindowEvent) {
        exitProcess(0)
      }
    })
    setSize(WIDTH, HEIGHT)
    isVisible = true
  }

  companion object {
    private const val WIDTH = 2000
    private const val HEIGHT = 1500
  }
}