package itmo.mervap.hw6.token

import itmo.mervap.hw6.visitor.TokenVisitor

abstract class Parenthesis : Token {
  override fun accept(visitor: TokenVisitor) {
    visitor.visit(this)
  }
}