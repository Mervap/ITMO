package ru.rain.ifmo

import ru.rain.ifmo.lexer.Lexer
import ru.rain.ifmo.parser.Parser
import java.awt.Dimension
import java.awt.Toolkit
import java.io.File
import javax.swing.JFrame

fun main(args: Array<String>) {
  val dropEmpty = if (args.size > 1) args[1].toBoolean() else false
  val frame = Parser(Lexer(File(args[0]).inputStream()), dropEmpty).parse().graphView()
  val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
  frame.setSize(screenSize.width, screenSize.height)
  frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
  frame.preferredSize = Dimension(100, 100)
  frame.isVisible = true
}