package visualization

import pascalLogic.PascalLogicLexer
import pascalLogic.PascalLogicParser
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.JFrame

fun main() {
  val root = PascalLogicParser(PascalLogicLexer("a or b and c or d")).e()
  val frame = VisualizeTree.graphView(root)
  val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
  frame.setSize(screenSize.width, screenSize.height)
  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  frame.preferredSize = Dimension(100, 100)
  frame.isVisible = true
}