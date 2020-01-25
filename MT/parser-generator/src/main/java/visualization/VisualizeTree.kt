package visualization

import com.mxgraph.layout.mxCompactTreeLayout
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import pascalLogic.Token
import pascalLogic.Tree
import pascalLogic.TreeBase
import javax.swing.JFrame

object VisualizeTree {
  fun graphView(tree: TreeBase): JFrame {
    val frame = JFrame()
    val graph = mxGraph()
    graph.model.beginUpdate()
    val root = internalGraphView(tree, graph)

    val layout = mxCompactTreeLayout(graph, false)
    layout.execute(frame.parent, root)
    graph.model.endUpdate()
    val graphComponent = mxGraphComponent(graph)

    frame.contentPane.add(graphComponent)
    return frame
  }

  private fun internalGraphView(tree: TreeBase, graph: mxGraph): Any {
    val defaultParent = graph.defaultParent
    val me = graph.insertVertex(defaultParent, null, (tree as Tree).nodeType, 300.0, 40.0, CELL_WIDTH, CELL_HEIGHT)

    for (ch in tree.children) {
      if (ch is Tree) {
        val chCell = internalGraphView(ch, graph)
        graph.insertEdge(defaultParent, null, "", me, chCell)
      } else {
        val name = (ch as Token).text.trim()
        val chCell = graph.insertVertex(defaultParent, null, "'$name'",
          200.0, 40.0, CELL_WIDTH, CELL_HEIGHT, "fillColor=#F5ECDD; fontSize=50")
        graph.insertEdge(defaultParent, null, "", me, chCell)
      }
    }
    return me
  }

  private const val CELL_WIDTH = 30.0
  private const val CELL_HEIGHT = 30.0
}