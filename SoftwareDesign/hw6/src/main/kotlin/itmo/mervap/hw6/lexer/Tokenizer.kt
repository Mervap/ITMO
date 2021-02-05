package itmo.mervap.hw6.lexer

import itmo.mervap.hw6.token.*
import java.io.InputStream

class Tokenizer(inputStream: InputStream) {
  private val reader: CharReader = CharReader(inputStream)
  private val buffer = StringBuffer()
  private var state: TokenizerState = TokenizerState.START
  var lastToken: Token? = null
    private set

  init {
    reader.readNext()
    advance()
  }

  fun advance() {
    var char = reader.curChar
    when {
      char == null -> advanceInner(TokenizerState.EOF)
      char == '(' -> advanceInner(TokenizerState.OPEN_BRACE)
      char == ')' -> advanceInner(TokenizerState.CLOSE_BRACE)
      char == '+' -> advanceInner(TokenizerState.PLUS)
      char == '-' -> advanceInner(TokenizerState.MINUS)
      char == '*' -> advanceInner(TokenizerState.MUL)
      char == '/' -> advanceInner(TokenizerState.DIV)
      char == ' ' -> {
        while (char == ' ') {
          reader.readNext()
          char = reader.curChar
        }
        advance()
        return
      }
      char.isDigit() -> {
        advanceInner(TokenizerState.NUMBER)
        while (char?.isDigit() == true) {
          buffer.append(char)
          reader.readNext()
          char = reader.curChar
        }
        return
      }
      else -> throw RuntimeException("Unexpected char '$char'")
    }
    reader.readNext()
  }

  private fun advanceInner(newState: TokenizerState) {
    lastToken = when (state) {
      TokenizerState.OPEN_BRACE -> OpenParenthesis()
      TokenizerState.CLOSE_BRACE -> CloseParenthesis()
      TokenizerState.PLUS -> AddOperator()
      TokenizerState.MINUS -> SubOperator()
      TokenizerState.MUL -> MulOperator()
      TokenizerState.DIV -> DivOperator()
      TokenizerState.NUMBER -> {
        NumberToken(buffer.toString().toLong()).also { buffer.setLength(0) }
      }
      else -> null
    }
    state = newState
  }

  companion object {
    fun getAll(inputStream: InputStream): List<Token> {
      val result = mutableListOf<Token>()
      val tokenizer = Tokenizer(inputStream)
      tokenizer.advance()
      while (tokenizer.lastToken != null) {
        result.add(tokenizer.lastToken!!)
        tokenizer.advance()
      }
      return result
    }

    private enum class TokenizerState {
      START,
      OPEN_BRACE,
      CLOSE_BRACE,
      PLUS,
      MINUS,
      MUL,
      DIV,
      NUMBER,
      EOF
    }
  }
}