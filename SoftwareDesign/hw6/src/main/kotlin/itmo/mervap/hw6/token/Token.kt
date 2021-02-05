package itmo.mervap.hw6.token

import itmo.mervap.hw6.visitor.TokenVisitor

interface Token {
  fun accept(visitor: TokenVisitor)
}