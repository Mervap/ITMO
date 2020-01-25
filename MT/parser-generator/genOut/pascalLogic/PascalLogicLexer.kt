package pascalLogic

import java.util.*;
import java.lang.RuntimeException;


class PascalLogicLexer(private var text: String) {
  lateinit var token: Token
  
  private val lpRegex =  Regex("^((\\())")
  private val rpRegex =  Regex("^((\\)))")
  private val orRegex =  Regex("^((or))")
  private val xorRegex =  Regex("^((xor))")
  private val andRegex =  Regex("^((and))")
  private val notRegex =  Regex("^((not))")
  private val boolRegex =  Regex("^((true)|(false))")
  private val variableRegex =  Regex("^(([a-zA-Z][a-zA-Z0-9]*))")
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
    res = orRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.OR, res.value, null)
      return
    }
    res = xorRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.XOR, res.value, null)
      return
    }
    res = andRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.AND, res.value, null)
      return
    }
    res = notRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.NOT, res.value, null)
      return
    }
    res = boolRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.BOOL, res.value, null)
      return
    }
    res = variableRegex.find(text)
    if (res != null) {
      text = text.drop(res.value.length)
      token = Token(TOKEN_TYPE.VARIABLE, res.value, null)
      return
    }
    throw ParserException("Unexpected string, no available lexemes at position ${sumLen - text.length}")
  }
}