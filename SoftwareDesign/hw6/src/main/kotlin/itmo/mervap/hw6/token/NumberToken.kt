package itmo.mervap.hw6.token

import itmo.mervap.hw6.visitor.TokenVisitor

class NumberToken(val number: Long) : Token {
  override fun accept(visitor: TokenVisitor) {
    visitor.visit(this)
  }
}