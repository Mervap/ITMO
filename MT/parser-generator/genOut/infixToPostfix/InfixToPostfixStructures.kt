package infixToPostfix

interface TreeBase {
  var res: Any?
  val text: String
}

class ParserException(message: String) : RuntimeException(message)

enum class TOKEN_TYPE {
  NUMBER,
  PLUS,
  MINUS,
  MUL,
  DIV,
  LP,
  RP,
  EMPTY,
  ENDF
}

data class Token(val type: TOKEN_TYPE, override val text: String, override var res: Any?) : TreeBase

enum class NODE {
  E,
  E1,
  T,
  T1,
  F,
}

class Tree(val nodeType: NODE, val children: List<TreeBase>, override var res: Any?) : TreeBase {
  override val text = children.joinToString(" ") { it.text }
}