package ru.rain.ifmo.parser

import com.mxgraph.layout.mxCompactTreeLayout
import com.mxgraph.model.mxCell
import com.mxgraph.swing.mxGraphComponent
import com.mxgraph.view.mxGraph
import ru.rain.ifmo.GrammarBase
import ru.rain.ifmo.lexer.Token
import javax.swing.JFrame


class Tree(private val nodeType: Node, private val children: List<GrammarBase>) : GrammarBase {

  fun graphView(): JFrame {
    val frame = JFrame()
    val graph = mxGraph()
    graph.model.beginUpdate()
    val root = internalGraphView(graph)

    val layout = mxCompactTreeLayout(graph, false)
    layout.execute(frame.parent, root)
    graph.model.endUpdate()
    val graphComponent = mxGraphComponent(graph)

    frame.contentPane.add(graphComponent)
    return frame
  }

  private fun internalGraphView(graph: mxGraph): Any {
    val defaultParent = graph.defaultParent
    val me = graph.insertVertex(defaultParent, null, nodeType.name, 300.0, 40.0, CELL_WIDTH, CELL_HEIGHT)

    for (ch in children) {
      if (ch is Tree) {
        val chCell = ch.internalGraphView(graph)
        graph.insertEdge(defaultParent, null, "", me, chCell)
      } else {
        val name = (ch as Token).toString().trim()
        val chCell = graph.insertVertex(defaultParent, null, "'$name'",
            200.0, 40.0, CELL_WIDTH, CELL_HEIGHT, "fillColor=#F5ECDD; fontSize=50")
        graph.insertEdge(defaultParent, null, "", me, chCell)
      }
    }
    return me
  }

  override fun equals(other: Any?): Boolean {
    if (other !is Tree) return false
    val otherChildren = other.children
    if (children.size != otherChildren.size) {
      return false
    }
    for (i in children.indices) {
      if (children[i] != otherChildren[i]) {
        return false
      }
    }
    return true
  }

  override fun toString(): String {
    val result = StringBuilder()
    innerToString(result)
    return result.trim().toString()
  }

  private fun innerToString(builder: StringBuilder) {
    for (ch in children) {
      when (ch) {
        is Tree -> ch.innerToString(builder)
        !is Token -> error("Wrong tree structure")
        else -> builder.append(ch.toString())
      }
    }
  }

  override fun hashCode(): Int {
    var result = nodeType.hashCode()
    result = 31 * result + children.hashCode()
    return result
  }

  companion object {
    private const val CELL_WIDTH = 30.0
    private const val CELL_HEIGHT = 30.0
  }
}

data class Rule(val left: Node, val right: List<GrammarBase>)

enum class Node : GrammarBase {
  EXPRESSION,
  EXPRESSION_H,
  CONJ,
  CONJ_H,
  XOR,
  XOR_H,
  NEG,
  VARIABLE,
  BUILTIN_CONSTANT
}