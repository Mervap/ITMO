package parserGrammar

class InputGrammar {
  var name: String = ""
  var header: String = ""

  private val _rules: MutableList<Rule> = mutableListOf()
  val rules: List<Rule>
    get() = _rules

  fun addRule(rule: Rule) {
    _rules.add(rule)
  }
}

class Rule {
  var name: String = ""

  private val _variants: MutableList<GrammarBase> = mutableListOf()
  val variants: List<GrammarBase>
    get() = _variants

  var params = ""
    private set

  var returns = ""
    private set

  fun addVariant(variant: GrammarBase) {
    _variants.add(variant)
  }

  fun parseParams(text: String) {
    params = text
  }

  fun parseReturns(text: String) {
    returns = text
  }
}

data class Lexeme(val regex: String, val isSkipped: Boolean = false) : GrammarBase

class Variant : GrammarBase {
  private val _children: MutableList<String> = mutableListOf()
  val children: List<String>
    get() = _children

  private val _params: MutableList<String> = mutableListOf()
  val params: List<String>
    get() = _params

  private val _code: MutableList<String> = mutableListOf()
  val code: List<String>
    get() = _code

  fun addChild(child: String, param: String?, code: String?) {
    _children.add(child)
    _params.add(param ?: "")
    _code.add(code ?: "")
  }
}

interface GrammarBase