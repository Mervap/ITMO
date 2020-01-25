package parserGenerator

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parserGrammar.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class ParserGenerator {

  private val first: MutableMap<String, MutableSet<String>> = mutableMapOf()
  private val firstVariant: MutableMap<Pair<String, Variant>, List<String>> = mutableMapOf()
  private val follow: MutableMap<String, MutableSet<String>> = mutableMapOf()
  private var followChanged: Boolean = true

  fun generate(filename: String, outputPath: String = "genOut") {
    val inputStream = CharStreams.fromFileName(filename)
    val lexer = GeneratorGrammarLexer(inputStream)
    val parser = GeneratorGrammarParser(CommonTokenStream(lexer))
    val grammar = parser.root().grammar
    val upperCamelCaseGrammarName = grammar.name
      .replace(Regex("^.")) { it.value.first().toUpperCase().toString() }
      .replace(Regex("_+[a-zA-Z]")) { it.value.last().toUpperCase().toString() }
    val lowerCamelCaseGrammarName = upperCamelCaseGrammarName
      .replace(Regex("^.")) { it.value.first().toLowerCase().toString() }

    val packagePath = Path.of(outputPath, lowerCamelCaseGrammarName).toString()
    generateLexer(grammar, packagePath, upperCamelCaseGrammarName, lowerCamelCaseGrammarName)
    generateParser(grammar, packagePath, upperCamelCaseGrammarName, lowerCamelCaseGrammarName)
  }

  private fun generateLexer(grammar: InputGrammar, outputPath: String, grammarName: String, packageName: String) {
    val lexerRules = grammar.rules.filter { it.variants.first() is Lexeme }
    val skippedRules = lexerRules.filter { (it.variants.first() as Lexeme).isSkipped }
    val normalRules = lexerRules - skippedRules
    val names = normalRules.map { it.name }
    if (!File(outputPath).exists()) {
      Files.createDirectories(Path.of(outputPath))
    }
    Path.of(outputPath, "${grammarName}$STRUCTURES.kt").toFile().writeText(buildString {
      // Package
      append("package $packageName\n\n")

      // Some data
      append("interface TreeBase {\n  var res: Any?\n  val text: String\n}\n")
      append("\nclass ParserException(message: String) : RuntimeException(message)\n")

      // Token
      append("\nenum class TOKEN_TYPE {\n")
      append(names.joinToString("") { "  $it,\n" })
      append("  $EMPTY,\n  $END\n")
      append("}\n\n")
      append("data class Token(val type: TOKEN_TYPE, override val text: String, override var res: Any?) : TreeBase")
      append("\n\n")
    })

    val className = grammarName + LEXER
    Path.of(outputPath, "$className.kt").toFile().writeText(buildString {
      // Package
      append("package $packageName\n\n")

      // Imports
      append("import java.util.*;\nimport java.lang.RuntimeException;\n")
      append(grammar.header)
      append("\n\n")

      // Lexer
      append("class $className(private var text: String)")
      val skipRegex = "^(${skippedRules.joinToString("|") {
        "(" + (it.variants.first() as Lexeme).regex.replace(
          "\\",
          "\\\\"
        ) + ")"
      }})"
      val regexs = normalRules.map { rule ->
        rule.name to "^(${rule.variants.joinToString("|") {
          "(" + (it as Lexeme).regex.replace(
            "\\",
            "\\\\"
          ) + ")"
        }})"
      }
      append(""" {
      |  lateinit var token: Token
      |  
      |${regexs.joinToString("\n") { "  private val ${it.first.toLowerCase()}Regex =  Regex(\"${it.second}\")" }}
      |  private val skipRegex = Regex("$skipRegex")
      |  private val sumLen = text.length
      |  
      |  init {
      |    nextToken()
      |  }
      |
      |  @Throws(ParserException::class)
      |  fun nextToken() {
      |    var res: MatchResult? = skipRegex.find(text)
      |    while (res != null) {
      |      text = text.drop(res.value.length)
      |      res = skipRegex.find(text)
      |    }
      |    if (text.isEmpty()) {
      |      token = Token(TOKEN_TYPE.$END, "", null)
      |      return
      |    }
      """.trimMargin()
      )
      normalRules.forEach {
        append("""
        |
        |    res = ${it.name.toLowerCase()}Regex.find(text)
        |    if (res != null) {
        |      text = text.drop(res.value.length)
        |      token = Token(TOKEN_TYPE.${it.name}, res.value, null)
        |      return
        |    }
        """.trimMargin()
        )
      }
      append("\n    throw ParserException(\"Unexpected string, no available lexemes at position ${'$'}{sumLen - text.length}\")")
      append("\n  }\n}")
    })
  }

  private fun generateParser(grammar: InputGrammar, outputPath: String, grammarName: String, packageName: String) {
    val parserRules = grammar.rules.filter { it.variants.first() !is Lexeme }
    computeFirst(parserRules)
    grammar.rules.firstOrNull()?.let { follow[it.name] = mutableSetOf(END) }
    computeFollow(parserRules)

    Path.of(outputPath, "${grammarName}$STRUCTURES.kt").toFile().appendText(buildString {
      // Tree
      append("enum class NODE {\n")
      append(parserRules.map { it.name.toUpperCase() }.joinToString("") { "  $it,\n" })
      append("}\n\n")
      append("class Tree(val nodeType: NODE, val children: List<TreeBase>, override var res: Any?) : TreeBase {\n  override val text = children.joinToString(\" \") { it.text }\n}")

    })

    val className = grammarName + PARSER
    Path.of(outputPath, "$className.kt").toFile().writeText(buildString {
      // Package
      append("package $packageName\n\n")
      // Imports
      append("import java.util.*;\nimport java.lang.RuntimeException;\n")
      append(grammar.header)
      append("\n\n")
      // Parser
      append("class $className(private val lexer: $grammarName$LEXER) {\n")
      parserRules.forEach { rule ->
        append("""
        |
        |  @Throws(ParserException::class)
        |  fun ${rule.name}(${rule.params}) : Tree {
        |    val curAttr = mutableMapOf<String, TreeBase>()
        |    val res: ${if (rule.returns.isNotEmpty()) rule.returns else "Any? = null"}
        |    val children = mutableListOf<TreeBase>()
        |    when (val tokenType = lexer.token.type) {
        """.trimMargin()
        )

        rule.variants.forEach { variant ->
          val singleRule = rule.name to variant as Variant
          val fst = firstVariant[singleRule]!!
          val all = fst.filter { it != EMPTY } + if (EMPTY in fst) follow[rule.name]!! else emptyList()
          append("\n      ").append(all.joinToString(", ") { "TOKEN_TYPE.${if (it == END) END else it}" })
            .append(" -> {")
          var was = false
          variant.children.forEachIndexed { ind, it ->
            when {
              it.isEmpty() -> {
                append("""
                        |
                        |        children.add(Token(TOKEN_TYPE.$EMPTY, "", null))
                        |        ${variant.code[ind].replaceAttr()}
                        """.trimMargin()
                )
              }
              it.first().isLowerCase() -> {
                append("""
                        |
                        |        ${if (!was) { was = true; "var " } else ""}tree = $it(${variant.params[ind].replaceAttr()})
                        |        children.add(tree)
                        |        curAttr["$it$ind"] = tree
                        |        ${variant.code[ind].replaceAttr()}
                        |        
                        """.trimMargin()
                )
              }
              else -> {
                append("""
                        |
                        |        if (lexer.token.type != TOKEN_TYPE.$it) {
                        |          throw ParserException("Expected: $it\nActual: ${'$'}{ lexer.token.type }")
                        |        }
                        |        children.add(lexer.token)
                        |        curAttr["$it$ind"] = lexer.token
                        |        lexer.nextToken()
                        |        ${variant.code[ind].replaceAttr()}
                        """.trimMargin()
                )
              }
            }
          }
          append("\n      }\n")
        }
        append("      else -> throw ParserException(\"Unexpected token ${'$'}{lexer.token.type}\")\n    }\n")
        append("    return Tree(NODE.${rule.name.toUpperCase()}, children, res)\n  }\n")
      }
      append("}\n")
    })
  }

  private fun computeFollow(rules: List<Rule>): Map<String, Set<String>> {
    while (followChanged) {
      followChanged = false
      for (rule in rules) {
        for (variant in rule.variants.map { it as Variant }) {
          val right = variant.children
          for (i in right.indices) {
            val cur = right[i]
            if (cur.isEmpty() || cur.first().isUpperCase()) continue
            val fst = computeFirstForExpr(right.drop(i + 1), rules)
            if (follow[cur] == null) {
              follow[cur] = mutableSetOf()
            }
            followChanged = followChanged || follow[cur]!!.addAll(fst.filter { it != EMPTY })
            if (EMPTY in fst) {
              followChanged = followChanged || follow[cur]!!.addAll(follow[rule.name] ?: continue)
            }
          }
        }
      }
    }
    return follow
  }


  private fun computeFirst(rules: List<Rule>): Map<String, Set<String>> {
    if (first.isNotEmpty()) return first

    for (term in rules) {
      computeFirstForTerm(term.name, rules)
    }
    return first
  }

  private fun computeFirstForTerm(termName: String, rules: List<Rule>): Set<String> {
    first[termName]?.let { return it }
    first[termName] = mutableSetOf()

    val rule = rules.find { it.name == termName } ?: return emptySet()
    for (term in rule.variants.map { it as Variant }) {
      firstVariant[termName to term] = computeFirstForExpr(term.children, rules)
      first[termName]!!.addAll(firstVariant[termName to term]!!)
    }
    return first[termName]!!
  }

  private fun computeFirstForExpr(expr: List<String>, rules: List<Rule>): List<String> {
    val left = expr.firstOrNull { it.isNotEmpty() } ?: return listOf(EMPTY)
    if (left.first().isUpperCase()) return listOf(left)

    val firstLeft = computeFirstForTerm(left, rules)
    var firstRight = emptyList<String>()
    if (EMPTY in firstLeft) {
      firstRight = computeFirstForExpr(expr.drop(1), rules)
    }
    return firstLeft.filter { it != EMPTY } + firstRight
  }

  companion object {
    private const val END = "ENDF"
    private const val EMPTY = "EMPTY"
    private const val STRUCTURES = "Structures"
    private const val PARSER = "Parser"
    private const val LEXER = "Lexer"

    private fun String.replaceAttr(): String {
      return replace(
        "${'$'}res",
        "res"
      ).replace(Regex("\\$[a-zA-Z][a-zA-Z0-9_]*")) { "curAttr[\"${it.value.drop(1)}\"]!!" }
    }
  }
}