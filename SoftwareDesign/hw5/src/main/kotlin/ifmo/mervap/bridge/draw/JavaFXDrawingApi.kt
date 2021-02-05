package ifmo.mervap.bridge.draw

import ifmo.mervap.bridge.graph.Graph
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage
import java.io.File

class JavaFXDrawingApi : Application(), DrawingApi {

  private val root = Group()

  override fun getDrawingAreaWidth() = WIDTH

  override fun getDrawingAreaHeight() = HEIGHT

  override fun drawCircle(centerX: Double, centerY: Double, radius: Double) {
    val circle = Circle(centerX, centerY, radius)
    root.children.add(circle)
  }

  override fun drawLine(x1: Double, y1: Double, x2: Double, y2: Double) {
    val line = Line(x1, y1, x2, y2)
    root.children.add(line)
  }

  override fun start(primaryStage: Stage) {
    val clazzName = parameters.raw[0]
    val graph: Graph
    try {
      graph = Graph.instantiateGraph(Class.forName(clazzName).asSubclass(Graph::class.java), this)
    } catch (e: ClassNotFoundException) {
      throw RuntimeException("Class '$clazzName' not found")
    }
    val filepath = parameters.raw[1]
    graph.readGraph(File(filepath).inputStream())
    graph.drawGraph()

    primaryStage.title = "JavaFX graph ($filepath)"
    primaryStage.width = WIDTH
    primaryStage.height = HEIGHT
    primaryStage.scene = Scene(root)
    primaryStage.show()
  }

  override fun show(graphClass: Class<out Graph>, filepath: String) = launch(graphClass.name, filepath)

  companion object {
    private const val WIDTH = 2000.0
    private const val HEIGHT = 1500.0
  }
}