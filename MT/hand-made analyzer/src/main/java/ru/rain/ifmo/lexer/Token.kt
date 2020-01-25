package ru.rain.ifmo.lexer

import ru.rain.ifmo.GrammarBase

enum class Token : GrammarBase {
  NOT,
  AND,
  XOR,
  OR,
  VARIABLE,
  TRUE,
  FALSE,
  END,
  OPEN,
  CLOSE,
  EMPTY;

  override fun toString(): String {
    return when (this) {
      OPEN -> "("
      CLOSE -> ")"
      VARIABLE -> "v"
      TRUE -> "true"
      FALSE -> "false"
      EMPTY -> ""
      else -> " ${name.toLowerCase()} "
    }
  }
}