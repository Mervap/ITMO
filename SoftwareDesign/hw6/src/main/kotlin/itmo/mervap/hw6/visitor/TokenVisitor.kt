package itmo.mervap.hw6.visitor

import itmo.mervap.hw6.token.*

interface TokenVisitor {
  fun visit(token: NumberToken)
  fun visit(token: Parenthesis)
  fun visit(token: Operator)
}