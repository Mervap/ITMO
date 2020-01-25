package infixToPostfix

import java.util.*;
import java.lang.RuntimeException;


class InfixToPostfixLexer(private var text: String) {
  lateinit var token: Token
  
  private val numberRegex =  Regex("^(([0-9]+))")
  private val plusRegex =  Regex("^((\\+))")
  private val minusRegex =  Regex("^((-))")
  private val mulRegex =  Regex("^((\\*))")
  private val divRegex =  Regex("^((/))")
  private val lpRegex =  Regex("^((\\())")
  private val rpRegex =  Regex("^((\\)))")
  private val skipRegex = Regex("^(([ \\n\\t\\r]+))")
  private val sumLen = text.length
  
  init {
    nextToken()
  }

  @Throws(ParserException::class)
  fun nextToken() {
    var res: MatchResult? = skipRegex.find(text)
    while (res != null) {
      text = text.drop(res.value.length)
      res = skipRegex.find(text)
    }
    if (text.isEmpty()) {
      token = Token(TOKEN_TYPE.ENDF, "", null)
      return
    }
    res = numberRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.NUMBER, res.value, null)
      return
    }
    res = plusRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.PLUS, res.value, null)
      return
    }
    res = minusRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.MINUS, res.value, null)
      return
    }
    res = mulRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.MUL, res.value, null)
      return
    }
    res = divRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.DIV, res.value, null)
      return
    }
    res = lpRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.LP, res.value, null)
      return
    }
    res = rpRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.RP, res.value, null)
      return
    }
    throw ParserException("Unexpected string, no available lexemes at position ${sumLen - text.length}")
  }
}