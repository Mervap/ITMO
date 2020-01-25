package pascalLogic

interface TreeBase {
  var res: Any?
  val text: String
}

class ParserException(message: String) : RuntimeException(message)

enum class TOKEN_TYPE {
  LP,
  RP,
  OR,
  XOR,
  AND,
  NOT,
  BOOL,
  VARIABLE,
  EMPTY,
  ENDF
}

data class Token(val type: TOKEN_TYPE, override val text: String, override var res: Any?) : TreeBase

enum class NODE {
  E,
  E1,
  X,
  X1,
  C,
  C1,
  N,
  V,
  B,
}

class Tree(val nodeType: NODE, val children: List<TreeBase>, override var res: Any?) : TreeBase {
  override val text = children.joinToString(" ") { it.text }
}