package itmo.mervap.hw6.visitor

import itmo.mervap.hw6.token.*
import java.util.*

class CalcVisitor(private val tokens: List<Token>) : TokenVisitor {

  private var cur = 0
  private fun nextToken(): Token = tokens[cur++]
  private val stack = Stack<Long>()

  fun calc(): Long {
    visit(nextToken())
    return stack.pop()
  }

  override fun visit(token: NumberToken) {
    stack.push(token.number)
  }

  override fun visit(token: Parenthesis) {
    throw RuntimeException("Can calc only pol")
  }

  override fun visit(token: Operator) {
    val right = stack.pop()
    val left = stack.pop()
    stack.add(token.apply(left, right))
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