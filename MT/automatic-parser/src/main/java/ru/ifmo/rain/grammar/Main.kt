package ru.ifmo.rain.grammar

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main() {
  val inputStream = CharStreams.fromFileName("input")
  val lexer = ArithmeticLexer(inputStream)
  val parser = ArithmeticParser(CommonTokenStream(lexer))
  parser.root()
  parser.allAssignments.forEach { (name, value) ->
    println("$name = $value")
  }
}