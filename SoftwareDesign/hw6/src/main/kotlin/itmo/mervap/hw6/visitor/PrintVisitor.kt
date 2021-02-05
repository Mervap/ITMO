package itmo.mervap.hw6.visitor

import itmo.mervap.hw6.token.*

class PrintVisitor(private val tokens: List<Token>) : TokenVisitor {

  private var cur = 0
  private fun nextToken(): Token = tokens[cur++]

  fun printAll() {
    visit(nextToken())
    println()
  }

  override fun visit(token: NumberToken) {
    print(token.number)
    print(" ")
  }

  override fun visit(token: Parenthesis) {
    when (token) {
      is OpenParenthesis -> print("( ")
      else -> print(") ")
    }
  }

  override fun visit(token: Operator) {
    when (token) {
      is AddOperator -> print("+ ")
      is SubOperator -> print("- ")
      is MulOperator -> print("* ")
      is DivOperator -> print("/ ")
    }
  }

  private fun visit(token: Token) {
    when(token) {
      is Operator -> visit(token)
      is NumberToken -> visit(token)
      is Parenthesis -> visit(token)
      else -> throw RuntimeException("Unexpected token $token")
    }
    if (cur < tokens.size) {
      visit(nextToken())
    }
  }
}