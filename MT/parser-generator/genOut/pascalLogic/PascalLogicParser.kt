package pascalLogic

import java.util.*;
import java.lang.RuntimeException;


class PascalLogicParser(private val lexer: PascalLogicLexer) {

  @Throws(ParserException::class)
  fun e() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NOT, TOKEN_TYPE.VARIABLE, TOKEN_TYPE.BOOL, TOKEN_TYPE.LP -> {
        var tree = x()
        children.add(tree)
        curAttr["x0"] = tree
        
        
        tree = e1()
        children.add(tree)
        curAttr["e11"] = tree
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.E, children, res)
  }

  @Throws(ParserException::class)
  fun e1() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.OR -> {
        if (lexer.token.type != TOKEN_TYPE.OR) {
          throw ParserException("Expected: OR\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["OR0"] = lexer.token
        lexer.nextToken()
        
        var tree = x()
        children.add(tree)
        curAttr["x1"] = tree
        
        
        tree = e1()
        children.add(tree)
        curAttr["e12"] = tree
        
        
      }

      TOKEN_TYPE.ENDF, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.E1, children, res)
  }

  @Throws(ParserException::class)
  fun x() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NOT, TOKEN_TYPE.VARIABLE, TOKEN_TYPE.BOOL, TOKEN_TYPE.LP -> {
        var tree = c()
        children.add(tree)
        curAttr["c0"] = tree
        
        
        tree = x1()
        children.add(tree)
        curAttr["x11"] = tree
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.X, children, res)
  }

  @Throws(ParserException::class)
  fun x1() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.XOR -> {
        if (lexer.token.type != TOKEN_TYPE.XOR) {
          throw ParserException("Expected: XOR\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["XOR0"] = lexer.token
        lexer.nextToken()
        
        var tree = c()
        children.add(tree)
        curAttr["c1"] = tree
        
        
        tree = x1()
        children.add(tree)
        curAttr["x12"] = tree
        
        
      }

      TOKEN_TYPE.OR, TOKEN_TYPE.ENDF, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.X1, children, res)
  }

  @Throws(ParserException::class)
  fun c() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NOT, TOKEN_TYPE.VARIABLE, TOKEN_TYPE.BOOL, TOKEN_TYPE.LP -> {
        var tree = n()
        children.add(tree)
        curAttr["n0"] = tree
        
        
        tree = c1()
        children.add(tree)
        curAttr["c11"] = tree
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.C, children, res)
  }

  @Throws(ParserException::class)
  fun c1() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.AND -> {
        if (lexer.token.type != TOKEN_TYPE.AND) {
          throw ParserException("Expected: AND\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["AND0"] = lexer.token
        lexer.nextToken()
        
        var tree = n()
        children.add(tree)
        curAttr["n1"] = tree
        
        
        tree = c1()
        children.add(tree)
        curAttr["c12"] = tree
        
        
      }

      TOKEN_TYPE.XOR, TOKEN_TYPE.OR, TOKEN_TYPE.ENDF, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.C1, children, res)
  }

  @Throws(ParserException::class)
  fun n() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NOT -> {
        if (lexer.token.type != TOKEN_TYPE.NOT) {
          throw ParserException("Expected: NOT\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["NOT0"] = lexer.token
        lexer.nextToken()
        
        var tree = v()
        children.add(tree)
        curAttr["v1"] = tree
        
        
      }

      TOKEN_TYPE.VARIABLE, TOKEN_TYPE.BOOL, TOKEN_TYPE.LP -> {
        var tree = v()
        children.add(tree)
        curAttr["v0"] = tree
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.N, children, res)
  }

  @Throws(ParserException::class)
  fun v() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.VARIABLE -> {
        if (lexer.token.type != TOKEN_TYPE.VARIABLE) {
          throw ParserException("Expected: VARIABLE\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["VARIABLE0"] = lexer.token
        lexer.nextToken()
        
      }

      TOKEN_TYPE.BOOL -> {
        var tree = b()
        children.add(tree)
        curAttr["b0"] = tree
        
        
      }

      TOKEN_TYPE.LP -> {
        if (lexer.token.type != TOKEN_TYPE.LP) {
          throw ParserException("Expected: LP\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["LP0"] = lexer.token
        lexer.nextToken()
        
        var tree = e()
        children.add(tree)
        curAttr["e1"] = tree
        
        
        if (lexer.token.type != TOKEN_TYPE.RP) {
          throw ParserException("Expected: RP\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["RP2"] = lexer.token
        lexer.nextToken()
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.V, children, res)
  }

  @Throws(ParserException::class)
  fun b() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.BOOL -> {
        if (lexer.token.type != TOKEN_TYPE.BOOL) {
          throw ParserException("Expected: BOOL\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["BOOL0"] = lexer.token
        lexer.nextToken()
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.B, children, res)
  }
}
