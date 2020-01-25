package variables

import java.util.*;
import java.lang.RuntimeException;


class VariablesParser(private val lexer: VariablesLexer) {

  @Throws(ParserException::class)
  fun root() : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>> = mutableListOf<Pair<String, Int>>() to mutableMapOf<String, Int>()
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.IDENTIFIER -> {
        var tree = assignment(res)
        children.add(tree)
        curAttr["assignment0"] = tree
        
        
        if (lexer.token.type != TOKEN_TYPE.SCOL) {
          throw ParserException("Expected: SCOL\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["SCOL1"] = lexer.token
        lexer.nextToken()
        
        tree = root_(res)
        children.add(tree)
        curAttr["root_2"] = tree
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.ROOT, children, res)
  }

  @Throws(ParserException::class)
  fun root_(context : Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Any? = null
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.IDENTIFIER -> {
        var tree = assignment(context)
        children.add(tree)
        curAttr["assignment0"] = tree
        
        
        if (lexer.token.type != TOKEN_TYPE.SCOL) {
          throw ParserException("Expected: SCOL\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["SCOL1"] = lexer.token
        lexer.nextToken()
        
        tree = root_(context)
        children.add(tree)
        curAttr["root_2"] = tree
        
        
      }

      TOKEN_TYPE.ENDF -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.ROOT_, children, res)
  }

  @Throws(ParserException::class)
  fun assignment(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.IDENTIFIER -> {
        if (lexer.token.type != TOKEN_TYPE.IDENTIFIER) {
          throw ParserException("Expected: IDENTIFIER\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["IDENTIFIER0"] = lexer.token
        lexer.nextToken()
        
        if (lexer.token.type != TOKEN_TYPE.EQ) {
          throw ParserException("Expected: EQ\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["EQ1"] = lexer.token
        lexer.nextToken()
        
        var tree = expression(context)
        children.add(tree)
        curAttr["expression2"] = tree
        
            val pair = curAttr["IDENTIFIER0"]!!.text to curAttr["expression2"]!!.res as Int
            context.second.put(pair.first, pair.second)
            context.first.add(pair)
            res = curAttr["expression2"]!!.res as Int
        
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.ASSIGNMENT, children, res)
  }

  @Throws(ParserException::class)
  fun expression(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NUMBER, TOKEN_TYPE.MINUS, TOKEN_TYPE.LP, TOKEN_TYPE.IDENTIFIER -> {
        var tree = term(context)
        children.add(tree)
        curAttr["term0"] = tree
        
        
        tree = expression1(context, curAttr["term0"]!!.res as Int)
        children.add(tree)
        curAttr["expression11"] = tree
         res = curAttr["expression11"]!!.res as Int 
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.EXPRESSION, children, res)
  }

  @Throws(ParserException::class)
  fun expression1(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, acc: Int) : Tree {
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
        
        var tree = term(context)
        children.add(tree)
        curAttr["term1"] = tree
        
        
        tree = expression1(context, acc + curAttr["term1"]!!.res as Int)
        children.add(tree)
        curAttr["expression12"] = tree
         res = curAttr["expression12"]!!.res as Int 
        
      }

      TOKEN_TYPE.MINUS -> {
        if (lexer.token.type != TOKEN_TYPE.MINUS) {
          throw ParserException("Expected: MINUS\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["MINUS0"] = lexer.token
        lexer.nextToken()
        
        var tree = term(context)
        children.add(tree)
        curAttr["term1"] = tree
        
        
        tree = expression1(context, acc - curAttr["term1"]!!.res as Int)
        children.add(tree)
        curAttr["expression12"] = tree
         res = curAttr["expression12"]!!.res as Int 
        
      }

      TOKEN_TYPE.SCOL, TOKEN_TYPE.MUL, TOKEN_TYPE.DIV, TOKEN_TYPE.PLUS, TOKEN_TYPE.MINUS, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
         res = acc 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.EXPRESSION1, children, res)
  }

  @Throws(ParserException::class)
  fun term(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.NUMBER, TOKEN_TYPE.MINUS, TOKEN_TYPE.LP, TOKEN_TYPE.IDENTIFIER -> {
        var tree = factor(context)
        children.add(tree)
        curAttr["factor0"] = tree
        
        
        tree = term1(context, curAttr["factor0"]!!.res as Int)
        children.add(tree)
        curAttr["term11"] = tree
         res = curAttr["term11"]!!.res as Int 
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.TERM, children, res)
  }

  @Throws(ParserException::class)
  fun term1(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, acc: Int) : Tree {
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
        
        var tree = factor(context)
        children.add(tree)
        curAttr["factor1"] = tree
        
        
        tree = term1(context, acc * curAttr["factor1"]!!.res as Int)
        children.add(tree)
        curAttr["term12"] = tree
         res = curAttr["term12"]!!.res as Int 
        
      }

      TOKEN_TYPE.DIV -> {
        if (lexer.token.type != TOKEN_TYPE.DIV) {
          throw ParserException("Expected: DIV\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["DIV0"] = lexer.token
        lexer.nextToken()
        
        var tree = factor(context)
        children.add(tree)
        curAttr["factor1"] = tree
        
        
        tree = term1(context, acc / curAttr["factor1"]!!.res as Int)
        children.add(tree)
        curAttr["term12"] = tree
         res = curAttr["term12"]!!.res as Int 
        
      }

      TOKEN_TYPE.PLUS, TOKEN_TYPE.MINUS, TOKEN_TYPE.SCOL, TOKEN_TYPE.MUL, TOKEN_TYPE.DIV, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
         res = acc 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.TERM1, children, res)
  }

  @Throws(ParserException::class)
  fun factor(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>) : Tree {
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
        
        var tree = expression(context)
        children.add(tree)
        curAttr["expression1"] = tree
         res = -(curAttr["expression1"]!!.res as Int) 
        
      }

      TOKEN_TYPE.LP -> {
        if (lexer.token.type != TOKEN_TYPE.LP) {
          throw ParserException("Expected: LP\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["LP0"] = lexer.token
        lexer.nextToken()
        
        var tree = expression(context)
        children.add(tree)
        curAttr["expression1"] = tree
        
        
        if (lexer.token.type != TOKEN_TYPE.RP) {
          throw ParserException("Expected: RP\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["RP2"] = lexer.token
        lexer.nextToken()
         res = curAttr["expression1"]!!.res as Int 
      }

      TOKEN_TYPE.IDENTIFIER -> {
        if (lexer.token.type != TOKEN_TYPE.IDENTIFIER) {
          throw ParserException("Expected: IDENTIFIER\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["IDENTIFIER0"] = lexer.token
        lexer.nextToken()
        
        var tree = identifier_contion(context, curAttr["IDENTIFIER0"]!!.text!!)
        children.add(tree)
        curAttr["identifier_contion1"] = tree
         res = curAttr["identifier_contion1"]!!.res as Int 
        
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.FACTOR, children, res)
  }

  @Throws(ParserException::class)
  fun identifier_contion(context: Pair<MutableList<Pair<String, Int>>, MutableMap<String, Int>>, name: String) : Tree {
    val curAttr = mutableMapOf<String, TreeBase>()
    val res: Int
    val children = mutableListOf<TreeBase>()
    when (val tokenType = lexer.token.type) {
      TOKEN_TYPE.EQ -> {
        if (lexer.token.type != TOKEN_TYPE.EQ) {
          throw ParserException("Expected: EQ\nActual: ${ lexer.token.type }")
        }
        children.add(lexer.token)
        curAttr["EQ0"] = lexer.token
        lexer.nextToken()
        
        var tree = expression(context)
        children.add(tree)
        curAttr["expression1"] = tree
        
             val pair = name to curAttr["expression1"]!!.res as Int
             context.second.put(pair.first, pair.second)
             context.first.add(pair)
             res = curAttr["expression1"]!!.res as Int
         
        
      }

      TOKEN_TYPE.MUL, TOKEN_TYPE.DIV, TOKEN_TYPE.PLUS, TOKEN_TYPE.MINUS, TOKEN_TYPE.SCOL, TOKEN_TYPE.RP -> {
        children.add(Token(TOKEN_TYPE.EMPTY, "", null))
         res = context.second.get(name)!! 
      }
      else -> throw ParserException("Unexpected token ${lexer.token.type}")
    }
    return Tree(NODE.IDENTIFIER_CONTION, children, res)
  }
}
