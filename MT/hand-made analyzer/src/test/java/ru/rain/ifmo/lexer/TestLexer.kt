package ru.rain.ifmo.lexer

import org.junit.Test
import ru.rain.ifmo.lexer.Token.*
import kotlin.random.Random
import kotlin.test.assertEquals

class TestLexer {

  @Test
  fun testExpression() {
    helper("(a and b) or not (c xor (a or not b))",
        listOf(OPEN, VARIABLE, AND, VARIABLE, CLOSE, OR, NOT, OPEN, VARIABLE,
            XOR, OPEN, VARIABLE, OR, NOT, VARIABLE, CLOSE, CLOSE, END))
  }

  @Test
  fun testWhitespaces() {
    helper("(a     and b) or     not (c \n\n \t\t xor   \t \n     (a     \n\n    or not b))",
        listOf(OPEN, VARIABLE, AND, VARIABLE, CLOSE, OR, NOT, OPEN, VARIABLE,
            XOR, OPEN, VARIABLE, OR, NOT, VARIABLE, CLOSE, CLOSE, END))
  }

  @Test
  fun testTrueFalse() {
    helper("(true or a) and (false or c) or d",
        listOf(OPEN, TRUE, OR, VARIABLE, CLOSE, AND, OPEN, FALSE, OR, VARIABLE, CLOSE, OR, VARIABLE, END))
  }

  @Test
  fun testUnderscore() {
    helper("a_b or c_d and camelCase_underscore",
        listOf(VARIABLE, OR, VARIABLE, AND, VARIABLE, END))
  }

  @Test
  fun testName() {
    helper("not nota or orb and ((dor and rand) xor xora)",
        listOf(NOT, VARIABLE, OR, VARIABLE, AND, OPEN, OPEN, VARIABLE, AND, VARIABLE, CLOSE, XOR, VARIABLE, CLOSE, END))
  }

  @Test
  fun testIncorrectExpression() {
    helper("a a a and or xor ((()",
        listOf(VARIABLE, VARIABLE, VARIABLE, AND, OR, XOR, OPEN, OPEN, OPEN, CLOSE, END))
  }


  private fun helper(expression: String, answer: List<Token>) {
    val l = Lexer(expression.byteInputStream())

    val tokens = mutableListOf<Token>()
    while (l.token != END) {
      l.nextToken()
      tokens.add(l.token)
    }
    assertEquals(answer, tokens)
  }
}