package itmo.mervap.hw6.visitor

import itmo.mervap.hw6.token.*
import java.util.*

class ParserException(message: String) : RuntimeException(message)

class ParserVisitor(private val tokens: List<Token>) : TokenVisitor {

  private var cur = 0
  private var braceBalance = 0
  private fun nextToken(): Token = tokens[cur++]

  private val resTokens = mutableListOf<Token>()
  private var operatorStack = Stack<Int>()

  fun getRevPolNotation(): List<Token> {
    if (resTokens.isNotEmpty()) return resTokens
    while (cur < tokens.size) {
      visit(nextToken())
    }
    if (braceBalance > 0) {
      throw ParserException("Non-closed opening parenthesis")
    }
    return resTokens
  }

  override fun visit(token: NumberToken) {
    resTokens.add(token)
    lookAhead()
  }

  override fun visit(token: Parenthesis) {
    if (token is CloseParenthesis) {
      if (braceBalance < 1) {
        throw ParserException("Unexpected closing parenthesis")
      }
      --braceBalance
      return
    }
    ++braceBalance
    val stack = operatorStack
    operatorStack = Stack<Int>()
    when (val next = nextToken()) {
      is NumberToken -> visit(next)
      is OpenParenthesis -> visit(next)
      is CloseParenthesis -> throw ParserException("Empty expression into parenthesis")
      is Operator -> throw ParserException("Unexpected operator after open parenthesis: ${next.name}")
      else -> throw RuntimeException("Unexpected token $token")
    }
    operatorStack = stack
    lookAhead()
  }

  override fun visit(token: Operator) {
    operatorStack.push(getOperatorPrior(token))
    if (cur == tokens.size) {
      throw ParserException("Expected operand after ${token.name}")
    }
    when (val next = nextToken()) {
      is NumberToken -> visit(next)
      is OpenParenthesis -> visit(next)
      is CloseParenthesis -> throw ParserException("Unexpected closing parenthesis after the operator ${token.name}")
      is Operator -> throw ParserException("Unexpected operator after operator: ${next.name} after ${token.name}")
      else -> throw RuntimeException("Unexpected token $token")
    }
    operatorStack.pop()
    resTokens.add(token)
  }

  private fun getOperatorPrior(token: Operator): Int {
    return when(token) {
      is AddOperator, is SubOperator -> 1
      else -> 2
    }
  }

  private fun lookAhead() {
    if (cur == tokens.size) return
    val next = tokens[cur]
    if (next is NumberToken) throw ParserException("Unexpected number: ${next.number}")
    if (operatorStack.size > 0) {
      if (next is Operator) {
        if (getOperatorPrior(next) < operatorStack.lastElement()) {
          return
        }
      }
    }
    visit(nextToken())
  }

  private fun visit(token: Token) {
    when(token) {
      is Operator -> visit(token)
      is NumberToken -> visit(token)
      is Parenthesis -> visit(token)
      else -> throw RuntimeException("Unexpected token $token")
    }
  }

  companion object {
    private val Token.name: String
      get() = this::class.java.simpleName
  }
}