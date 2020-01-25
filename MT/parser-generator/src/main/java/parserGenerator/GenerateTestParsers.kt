package parserGenerator

fun main() {
  ParserGenerator().generate("src/main/resources/calculator.g4")
  ParserGenerator().generate("src/main/resources/pascalLogic.g4")
  ParserGenerator().generate("src/main/resources/variables.g4")
  ParserGenerator().generate("src/main/resources/infixToPostfix.g4")
}