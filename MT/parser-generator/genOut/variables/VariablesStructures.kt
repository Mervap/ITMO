package variables

interface TreeBase {
  var res: Any?
  val text: String
}

class ParserException(message: String) : RuntimeException(message)

enum class TOKEN_TYPE {
  SCOL,
  LP,
  RP,
  EQ,
  PLUS,
  MINUS,
  MUL,
  DIV,
  NUMBER,
  IDENTIFIER,
  EMPTY,
  ENDF
}

data class Token(val type: TOKEN_TYPE, override val text: String, override var res: Any?) : TreeBase

enum class NODE {
  ROOT,
  ROOT_,
  ASSIGNMENT,
  EXPRESSION,
  EXPRESSION1,
  TERM,
  TERM1,
  FACTOR,
  IDENTIFIER_CONTION,
}

class Tree(val nodeType: NODE, val children: List<TreeBase>, override var res: Any?) : TreeBase {
  override val text = children.joinToString(" ") { it.text }
}