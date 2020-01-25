package ru.rain.ifmo.parser

import ru.rain.ifmo.GrammarBase
import ru.rain.ifmo.lexer.Lexer
import ru.rain.ifmo.lexer.Token

class Parser(private val lexer: Lexer, private val dropEmpty: Boolean = false) {

  private val first: MutableMap<Node, MutableSet<Token>> = mutableMapOf()
  private val firstRule: MutableMap<Rule, List<Token>> = mutableMapOf()
  private val follow: MutableMap<Node, MutableSet<Token>> = mutableMapOf()
  private var followChanged: Boolean = true

  private val nodeGenerator = mapOf(
      Node.EXPRESSION to { e() },
      Node.EXPRESSION_H to { eH() },
      Node.XOR to { x() },
      Node.XOR_H to { xH() },
      Node.CONJ to { c() },
      Node.CONJ_H to { cH() },
      Node.NEG to { n() },
      Node.VARIABLE to { v() },
      Node.BUILTIN_CONSTANT to { b() }
  )

  init {
    computeFirst()
    follow[Node.EXPRESSION] = mutableSetOf(Token.END)
    computeFollow()
  }

  fun parse(): Tree {
    lexer.nextToken()
    val result = e() ?: Tree(Node.EXPRESSION, emptyList())
    if (lexer.token != Token.END) {
      throw ParserException("Unexpected token ${lexer.token}")
    }
    return result
  }

  fun computeFollow(): Map<Node, Set<Token>> {
    while (followChanged) {
      followChanged = false
      for (rule in grammar.values.reduce { a, b -> a + b }) {
        val right = rule.right
        for (i in right.indices) {
          val cur = right[i]
          if (cur !is Node) continue
          val fst = computeFirstForExpr(right.drop(i + 1))
          if (follow[cur] == null) {
            follow[cur] = mutableSetOf()
          }
          followChanged = followChanged || follow[cur]!!.addAll(fst.filter { it != Token.EMPTY })
          if (Token.EMPTY in fst) {
            followChanged = followChanged || follow[cur]!!.addAll(follow[rule.left] ?: continue)
          }
        }
      }
    }
    return follow
  }


  fun computeFirst(): Map<Node, Set<Token>> {
    if (first.isNotEmpty()) return first

    for (node in grammar.keys) {
      computeFirstForNode(node)
    }
    return first
  }

  private fun e(): Tree? = baseParser(Node.EXPRESSION)
  private fun eH(): Tree? = baseParser(Node.EXPRESSION_H)
  private fun x(): Tree? = baseParser(Node.XOR)
  private fun xH(): Tree? = baseParser(Node.XOR_H)
  private fun c(): Tree? = baseParser(Node.CONJ)
  private fun cH(): Tree? = baseParser(Node.CONJ_H)
  private fun n(): Tree? = baseParser(Node.NEG)
  private fun v(): Tree? = baseParser(Node.VARIABLE)
  private fun b(): Tree? = baseParser(Node.BUILTIN_CONSTANT)

  private fun baseParser(node: Node): Tree? {
    val children = mutableListOf<GrammarBase>()
    for (rule in grammar.getValue(node)) {
      val fst = firstRule[rule]!!
      if (lexer.token in fst || if (Token.EMPTY in fst) lexer.token in follow[rule.left]!! else false) {
        for (base in rule.right) {
          when {
            base is Node -> {
              val child = nodeGenerator.getValue(base)()
              if (child != null) {
                children.add(child)
              }
            }
            base == Token.EMPTY && dropEmpty -> return null
            base == Token.EMPTY && !dropEmpty -> children.add(base)
            base != lexer.token -> throw ParserException("Expected: ${lexer.token}; Actual: $base")
            else -> {
              children.add(base)
              lexer.nextToken()
            }
          }
        }
        return Tree(node, children)
      }
    }
    throw ParserException("No rule")
  }

  private fun computeFirstForNode(node: Node): Set<Token> {
    first[node]?.let { return it }
    first[node] = mutableSetOf()

    for (rule in grammar.getValue(node)) {
      firstRule[rule] = computeFirstForExpr(rule.right)
      first[node]!!.addAll(computeFirstForExpr(rule.right))
    }
    return first[node]!!
  }

  private fun computeFirstForExpr(expr: List<GrammarBase>): List<Token> {
    val left = expr.firstOrNull() ?: return listOf(Token.EMPTY)
    if (left is Token) return listOf(left)

    val firstLeft = computeFirstForNode(left as Node)
    var firstRight = emptyList<Token>()
    if (Token.EMPTY in firstLeft) {
      firstRight = computeFirstForExpr(expr.drop(1))
    }
    return firstLeft.filter { it != Token.EMPTY } + firstRight
  }

  companion object {
    private val grammar = mapOf(
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
//            Rule(Node.XOR_H, listOf(Token.XOR, Node.CONJ, Node.XOR_H))
            Rule(Node.XOR_H, listOf(Token.XOR, Node.XOR))
        ),
        Node.CONJ to listOf(
            Rule(Node.CONJ, listOf(Node.NEG, Node.CONJ_H))
        ),
        Node.CONJ_H to listOf(
            Rule(Node.CONJ_H, listOf(Token.EMPTY)),
            Rule(Node.CONJ_H, listOf(Token.AND, Node.NEG, Node.CONJ_H))
        ),
        Node.NEG to listOf(
            Rule(Node.NEG, listOf(Token.NOT, Node.VARIABLE)),
            Rule(Node.NEG, listOf(Node.VARIABLE))
        ),
        Node.VARIABLE to listOf(
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

