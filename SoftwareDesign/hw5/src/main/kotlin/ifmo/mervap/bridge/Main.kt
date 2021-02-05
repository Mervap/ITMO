package ifmo.mervap.bridge

import ifmo.mervap.bridge.draw.AwtDrawingApi
import ifmo.mervap.bridge.draw.JavaFXDrawingApi
import ifmo.mervap.bridge.graph.AdjacencyMatrixGraph
import ifmo.mervap.bridge.graph.EdgeConnectivityGraph

fun main(args: Array<String>) {
  val apiType = when (args[0]) {
    "awt" -> AwtDrawingApi()
    "javafx" -> JavaFXDrawingApi()
    else -> throw RuntimeException("Unknown api type: ${args[0]}")
  }
  val (graphType, filepath) = when (args[1]) {
    "conn" -> EdgeConnectivityGraph::class.java to "input_conn.txt"
    "adj" -> AdjacencyMatrixGraph::class.java to "input_adj.txt"
    else -> throw RuntimeException("Unknown graph type: ${args[1]}")
  }
  apiType.show(graphType, filepath)
}