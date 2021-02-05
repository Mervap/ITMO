package itmo.mervap.hw6.token

import itmo.mervap.hw6.visitor.TokenVisitor

abstract class Operator : Token {

  abstract fun apply(left: Long, right: Long): Long

  override fun accept(visitor: TokenVisitor) {
    visitor.visit(this)
  }
}