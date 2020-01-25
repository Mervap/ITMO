package pascalLogic

import org.junit.Test
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random
import kotlin.test.assertEquals


class TestPascalLogic {

  private fun TreeBase.toTestTree(): GrammarBase? {
    if (this is pascalLogic.Token) {
      return when (type) {
        TOKEN_TYPE.LP -> Token.OPEN
        TOKEN_TYPE.RP -> Token.CLOSE
        TOKEN_TYPE.OR -> Token.OR
        TOKEN_TYPE.XOR -> Token.XOR
        TOKEN_TYPE.AND -> Token.AND
        TOKEN_TYPE.NOT -> Token.NOT
        TOKEN_TYPE.VARIABLE -> Token.VARIABLE
        TOKEN_TYPE.BOOL -> Token.TRUE
        TOKEN_TYPE.EMPTY -> Token.EMPTY
        TOKEN_TYPE.ENDF -> Token.END
      }
    }
    else {
      val tr = this as pascalLogic.Tree
      return when (tr.nodeType) {
        NODE.E -> Tree(Node.EXPRESSION, tr.children.mapNotNull { it.toTestTree() })
        NODE.E1 -> Tree(Node.EXPRESSION_H, tr.children.mapNotNull { it.toTestTree() })
        NODE.X -> Tree(Node.XOR, tr.children.mapNotNull { it.toTestTree() })
        NODE.X1 -> Tree(Node.XOR_H, tr.children.mapNotNull { it.toTestTree() })
        NODE.C -> Tree(Node.CONJ, tr.children.mapNotNull { it.toTestTree() })
        NODE.C1 -> Tree(Node.CONJ_H, tr.children.mapNotNull { it.toTestTree() })
        NODE.N -> Tree(Node.NEG, tr.children.mapNotNull { it.toTestTree() })
        NODE.V -> Tree(Node.VARIABLE, tr.children.mapNotNull { it.toTestTree() })
        NODE.B -> Tree(Node.BUILTIN_CONSTANT, tr.children.mapNotNull { it.toTestTree() })
        else -> null
      }
    }
  }


  @Test
  fun testParser() {
    var max = -1
    for (i in 0..100000) {
      val (input, result) = genTest()
      max = max(max, input.length)
      try {
        val parsed = PascalLogicParser(PascalLogicLexer(input)).e()
        assertEquals(result, parsed.toTestTree(), input)
      } catch (e: RuntimeException) {
        error(input)
      }
    }
    print("Max len of input: $max")
  }

  @Test
  fun testException() {
    exceptionTest("")
    exceptionTest("a or")
    exceptionTest("((a or b) and c")
    exceptionTest("a or or b")
    exceptionTest("true or false true")
    exceptionTest("true (and) false")
    exceptionTest("a or b ded")
  }

  @Test
  fun testTreeString() {
    val input = "(true or v) and (true or v) or v"
    val root = PascalLogicParser(PascalLogicLexer(input)).e()
    assertEquals(root.toTestTree().toString(), input)
  }

  private fun exceptionTest(input: String) {
    try {
      PascalLogicParser(PascalLogicLexer(input)).e()
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
      }
      else {
        val chTree = innerGenTest(el as Node, builder)
        children.add(chTree)
      }
    }
    return Tree(node, children)
  }

  companion object {

    private interface GrammarBase

    private data class Rule(val left: Node, val right: List<GrammarBase>)

    private enum class Node : GrammarBase {
      EXPRESSION,
      EXPRESSION_H,
      CONJ,
      CONJ_H,
      XOR,
      XOR_H,
      NEG,
      VARIABLE,
      BUILTIN_CONSTANT
    }

    private enum class Token : GrammarBase {
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

    private class Tree(private val nodeType: Node, private val children: List<GrammarBase>) : GrammarBase {
      override fun equals(other: Any?): Boolean {
        if (other !is Tree) return false
        val otherChildren = other.children
        if (children.size != otherChildren.size) {
          return false
        }
        for (i in children.indices) {
          if (children[i] != otherChildren[i]) {
            return false
          }
        }
        return true
      }

      override fun toString(): String {
        val result = StringBuilder()
        innerToString(result)
        return result.trim().toString()
      }

      private fun innerToString(builder: StringBuilder) {
        for (ch in children) {
          when (ch) {
            is Tree -> ch.innerToString(builder)
            !is Token -> error("Wrong tree structure")
            else -> builder.append(ch.toString())
          }
        }
      }

      override fun hashCode(): Int {
        var result = nodeType.hashCode()
        result = 31 * result + children.hashCode()
        return result
      }

      companion object {
        private const val CELL_WIDTH = 30.0
        private const val CELL_HEIGHT = 30.0
      }
    }


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
//        Rule(Node.XOR_H, listOf(Token.XOR, Node.XOR))
        Rule(Node.XOR_H, listOf(Token.XOR, Node.CONJ, Node.XOR_H))
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
        Rule(Node.BUILTIN_CONSTANT, listOf(Token.TRUE))
      )
    )
  }
}
