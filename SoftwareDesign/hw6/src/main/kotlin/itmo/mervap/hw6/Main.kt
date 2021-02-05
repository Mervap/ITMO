package itmo.mervap.hw6

import itmo.mervap.hw6.lexer.Tokenizer
import itmo.mervap.hw6.token.Token
import itmo.mervap.hw6.visitor.CalcVisitor
import itmo.mervap.hw6.visitor.ParserException
import itmo.mervap.hw6.visitor.ParserVisitor
import itmo.mervap.hw6.visitor.PrintVisitor
import java.io.File

fun main() {
  val tokens = Tokenizer.getAll(File("input.txt").inputStream())
  val pol: List<Token>
  try {
    pol = ParserVisitor(tokens).getRevPolNotation()
  }
  catch (e : ParserException) {
    print("Incorrect expression: ${e.message}")
    return
  }

  print("Reverse Polish notation: ")
  PrintVisitor(pol).printAll()

  print("Expression calculation result: ")
  println(CalcVisitor(pol).calc())
}