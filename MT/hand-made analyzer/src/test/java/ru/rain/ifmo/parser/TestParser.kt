package ru.rain.ifmo.parser

import org.junit.Test
import ru.rain.ifmo.GrammarBase
import ru.rain.ifmo.lexer.Lexer
import ru.rain.ifmo.lexer.Token
import java.io.InputStream
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random
import kotlin.test.assertEquals


class TestParser {

  @Test
  fun testParser() {
    var max = -1
    for (i in 0..100000) {
      val (input, result) = genTest()
      max = max(max, input.length)
      try {
        val parsed = Parser(Lexer(input.byteInputStream())).parse()
        assertEquals(result, parsed, input)
      } catch (e: ParserException) {
        error(input)
      }
    }
    print("Max len of input: $max")
  }

  @Test
  fun testException() {
    exceptionTest("")
    exceptionTest("a or")
    exceptionTest("(a or b))")
    exceptionTest("((a or b) and c")
    exceptionTest("a or or b")
    exceptionTest("true or false true")
    exceptionTest("true (and) false")
    exceptionTest("a or b ded")
  }

  @Test
  fun testFirst() {
    val first = Parser(Lexer(InputStream.nullInputStream())).computeFirst().toSortedMap()

/*    first.toSortedMap().forEach { (n, vv) ->
      print("$n: ")
      for (v in vv) {
        print("$v ")
      }
      println()
    }*/

    assert(first == mapOf(
        Node.EXPRESSION to setOf(Token.NOT, Token.VARIABLE, Token.TRUE, Token.FALSE, Token.OPEN),
        Node.EXPRESSION_H to setOf(Token.EMPTY, Token.OR),
        Node.CONJ to setOf(Token.NOT, Token.VARIABLE, Token.TRUE, Token.FALSE, Token.OPEN),
        Node.CONJ_H to setOf(Token.EMPTY, Token.AND),
        Node.XOR to setOf(Token.NOT, Token.VARIABLE, Token.TRUE, Token.FALSE, Token.OPEN),
        Node.XOR_H to setOf(Token.EMPTY, Token.XOR),
        Node.NEG to setOf(Token.NOT, Token.VARIABLE, Token.TRUE, Token.FALSE, Token.OPEN),
        Node.VARIABLE to setOf(Token.VARIABLE, Token.TRUE, Token.FALSE, Token.OPEN),
        Node.BUILTIN_CONSTANT to setOf(Token.TRUE, Token.FALSE)
    ))
  }

  @Test
  fun testFollow() {
    val follow = Parser(Lexer(InputStream.nullInputStream())).computeFollow().toSortedMap()

/*    follow.toSortedMap().forEach { (n, vv) ->
      print("$n: ")
      for (v in vv) {
        print("$v ")
      }
      println()
    }*/

    assert(follow == mapOf(
        Node.EXPRESSION to setOf(Token.END, Token.CLOSE),
        Node.EXPRESSION_H to setOf(Token.END, Token.CLOSE),
        Node.CONJ to setOf(Token.XOR, Token.OR, Token.END, Token.CLOSE),
        Node.CONJ_H to setOf(Token.XOR, Token.OR, Token.END, Token.CLOSE),
        Node.XOR to setOf(Token.OR, Token.END, Token.CLOSE),
        Node.XOR_H to setOf(Token.OR, Token.END, Token.CLOSE),
        Node.NEG to setOf(Token.AND, Token.XOR, Token.OR, Token.END, Token.CLOSE),
        Node.VARIABLE to setOf(Token.AND, Token.XOR, Token.OR, Token.END, Token.CLOSE),
        Node.BUILTIN_CONSTANT to setOf(Token.AND, Token.XOR, Token.OR, Token.END, Token.CLOSE)
    ))
  }

  @Test
  fun testTreeString() {
    val input = "(true or v) and (false or v) or v"
    val root = Parser(Lexer(input.byteInputStream())).parse()
    assertEquals(root.toString(), input)
  }

  private fun exceptionTest(input: String) {
    try {
      Parser(Lexer(input.byteInputStream())).parse()
      val mess = if (input.isEmpty()) "Empty input"
      else "'$input'"
      error("$mess don't throw exception")
    } catch (e: ParserException) {
      // ignore
    }
  }

  private fun genTest(): Pair<String, Tree> {
    val builder = StringBuilder()
    val tree = innerGenTest(Node.EXPRESSION, builder)
    return builder.trim().toString() to tree
  }

  private fun innerGenTest(node: Node, builder: StringBuilder): Tree {
    val rules = repetitiveGrammar.getValue(node)
    val rule = rules[abs(Random.nextInt() % rules.size)]
    val children = mutableListOf<GrammarBase>()
    for (el in rule.right) {
      if (el is Token) {
        children.add(el)
        builder.append(el.toString())
      } else {
        val chTree = innerGenTest(el as Node, builder)
        children.add(chTree)
      }
    }
    return Tree(node, children)
  }

  companion object {
    private val repetitiveGrammar = mapOf(
        Node.EXPRESSION to listOf(
            Rule(Node.EXPRESSION, listOf(Node.XOR, Node.EXPRESSION_H))
        ),
        Node.EXPRESSION_H to listOf(
            Rule(Node.EXPRESSION_H, listOf(Token.EMPTY)),
            Rule(Node.EXPRESSION_H, listOf(Token.OR, Node.XOR, Node.EXPRESSION_H))
        ),
        Node.XOR to listOf(
            Rule(Node.XOR, listOf(Node.CONJ, Node.XOR_H))
        ),
        Node.XOR_H to listOf(
            Rule(Node.XOR_H, listOf(Token.EMPTY)),
            Rule(Node.XOR_H, listOf(Token.XOR, Node.XOR))
//            Rule(Node.XOR_H, listOf(Token.XOR, Node.CONJ, Node.XOR_H))
        ),
        Node.CONJ to listOf(
            Rule(Node.CONJ, listOf(Node.NEG, Node.CONJ_H))
        ),
        Node.CONJ_H to listOf(
            Rule(Node.CONJ_H, listOf(Token.EMPTY)),
            Rule(Node.CONJ_H, listOf(Token.EMPTY)),
            Rule(Node.CONJ_H, listOf(Token.AND, Node.NEG, Node.CONJ_H))
        ),
        Node.NEG to listOf(
            Rule(Node.NEG, listOf(Token.NOT, Node.VARIABLE)),
            Rule(Node.NEG, listOf(Node.VARIABLE)),
            Rule(Node.NEG, listOf(Node.VARIABLE))
        ),
        Node.VARIABLE to listOf(
            Rule(Node.VARIABLE, listOf(Token.VARIABLE)),
            Rule(Node.VARIABLE, listOf(Node.BUILTIN_CONSTANT)),
            Rule(Node.VARIABLE, listOf(Token.VARIABLE)),
            Rule(Node.VARIABLE, listOf(Node.BUILTIN_CONSTANT)),
            Rule(Node.VARIABLE, listOf(Token.VARIABLE)),
            Rule(Node.VARIABLE, listOf(Node.BUILTIN_CONSTANT)),
            Rule(Node.VARIABLE, listOf(Token.OPEN, Node.EXPRESSION, Token.CLOSE))
        ),
        Node.BUILTIN_CONSTANT to listOf(
            Rule(Node.BUILTIN_CONSTANT, listOf(Token.TRUE)),
            Rule(Node.BUILTIN_CONSTANT, listOf(Token.FALSE))
        )
    )
  }
}