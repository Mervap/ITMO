package calculator

import java.util.*;
import java.lang.RuntimeException;


class CalculatorParser(private val lexer: CalculatorLexer) {

  @Throws(ParserException::class)
  fun e() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NUMBER, TOKEN_TYPE.MINUS, TOKEN_TYPE.LP -> {
        var tree = t()
        children.add(tree)
        curAttr["t0"] = tree
        
        
        tree = e1(curAttr["t0"]!!.res as Int)
        children.add(tree)
        curAttr["e11"] = tree
         res = curAttr["e11"]!!.res as Int 
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.E, children, res)
  }

  @Throws(ParserException::class)
  fun e1(acc: Int) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.PLUS -> {
        if (lexer.token.type != TOKEN_TYPE.PLUS) {
          throw ParserException("Expected: PLUS\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["PLUS0"] = lexer.token
        lexer.nextToken()
        
        var tree = t()
        children.add(tree)
        curAttr["t1"] = tree
        
        
        tree = e1(acc + curAttr["t1"]!!.res as Int)
        children.add(tree)
        curAttr["e12"] = tree
         res = curAttr["e12"]!!.res as Int 
        
      }

      TOKEN_TYPE.MINUS -> {
        if (lexer.token.type != TOKEN_TYPE.MINUS) {
          throw ParserException("Expected: MINUS\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["MINUS0"] = lexer.token
        lexer.nextToken()
        
        var tree = t()
        children.add(tree)
        curAttr["t1"] = tree
        
        
        tree = e1(acc - curAttr["t1"]!!.res as Int)
        children.add(tree)
        curAttr["e12"] = tree
         res = curAttr["e12"]!!.res as Int 
        
      }

      TOKEN_TYPE.ENDF, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
         res = acc 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.E1, children, res)
  }

  @Throws(ParserException::class)
  fun t() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NUMBER, TOKEN_TYPE.MINUS, TOKEN_TYPE.LP -> {
        var tree = f()
        children.add(tree)
        curAttr["f0"] = tree
        
        
        tree = t1(curAttr["f0"]!!.res as Int)
        children.add(tree)
        curAttr["t11"] = tree
         res = curAttr["t11"]!!.res as Int 
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.T, children, res)
  }

  @Throws(ParserException::class)
  fun t1(acc: Int) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.MUL -> {
        if (lexer.token.type != TOKEN_TYPE.MUL) {
          throw ParserException("Expected: MUL\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["MUL0"] = lexer.token
        lexer.nextToken()
        
        var tree = f()
        children.add(tree)
        curAttr["f1"] = tree
        
        
        tree = t1(acc * curAttr["f1"]!!.res as Int)
        children.add(tree)
        curAttr["t12"] = tree
         res = curAttr["t12"]!!.res as Int 
        
      }

      TOKEN_TYPE.DIV -> {
        if (lexer.token.type != TOKEN_TYPE.DIV) {
          throw ParserException("Expected: DIV\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["DIV0"] = lexer.token
        lexer.nextToken()
        
        var tree = f()
        children.add(tree)
        curAttr["f1"] = tree
        
        
        tree = t1(acc / curAttr["f1"]!!.res as Int)
        children.add(tree)
        curAttr["t12"] = tree
         res = curAttr["t12"]!!.res as Int 
        
      }

      TOKEN_TYPE.PLUS, TOKEN_TYPE.MINUS, TOKEN_TYPE.ENDF, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
         res = acc 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.T1, children, res)
  }

  @Throws(ParserException::class)
  fun f() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NUMBER -> {
        if (lexer.token.type != TOKEN_TYPE.NUMBER) {
          throw ParserException("Expected: NUMBER\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["NUMBER0"] = lexer.token
        lexer.nextToken()
         res = curAttr["NUMBER0"]!!.text.toInt() 
      }

      TOKEN_TYPE.MINUS -> {
        if (lexer.token.type != TOKEN_TYPE.MINUS) {
          throw ParserException("Expected: MINUS\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["MINUS0"] = lexer.token
        lexer.nextToken()
        
        var tree = f()
        children.add(tree)
        curAttr["f1"] = tree
         res = -(curAttr["f1"]!!.res as Int) 
        
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
         res = curAttr["e1"]!!.res as Int 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.F, children, res)
  }
}
