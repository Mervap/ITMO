package ru.rain.ifmo.lexer

import ru.rain.ifmo.parser.ParserException
import java.io.IOException
import java.io.InputStream

const val END_CHAR = (-1).toChar()

class Lexer(private val inputStream: InputStream) {

  var token: Token = Token.EMPTY
  private var pos: Long = 0
  private var char: Char = '\u0000'

  init {
    nextChar()
  }

  fun nextToken() {
    if (token == Token.END) {
      throw ParserException("End")
    }

    while (char.isWhitespace()) {
      nextChar()
    }

    val futureToken = StringBuilder()
    if (char.isLetterDigitOrUnderscore()) {
      while (char.isLetterDigitOrUnderscore()) {
        futureToken.append(char)
        nextChar()
      }
    } else if (char != END_CHAR) {
      futureToken.append(char)
      nextChar()
    }

    token = when (futureToken.toString()) {
      "not" -> Token.NOT
      "and" -> Token.AND
      "xor" -> Token.XOR
      "or" -> Token.OR
      "true" -> Token.TRUE
      "false" -> Token.FALSE
      "(" -> Token.OPEN
      ")" -> Token.CLOSE
      "" -> Token.END
      else -> Token.VARIABLE
    }
  }

  private fun nextChar() {
    ++pos
    try {
      char = inputStream.read().toChar()
    } catch (e: IOException) {
      throw ParserException("Error while reading")
    }
  }

  private fun Char.isLetterDigitOrUnderscore(): Boolean {
    return isLetterOrDigit() || this == '_'
  }
}