package infixToPostfix

import org.junit.Test
import kotlin.test.assertEquals

class TestInf {

  @Test
  fun testInfix() {
    doTest("3 + 4", "3 4 +")
    doTest("3 + 4 * 2 / (1 - 5) * 2", "3 4 2 * 1 5 - / 2 * +")
    doTest("3 + 9 + (9 + (9 * 1) - 9)", "3 9 + 9 9 1 * + 9 - +")
    doTest("78 * 6 - 3 + 9 / (2 - 1)", "78 6 * 3 - 9 2 1 - / +")
    doTest("(8 + 2 * 5) / (1 + 3 * 2 - 4)", "8 2 5 * + 1 3 2 * + 4 - /")
  }

  private fun doTest(input: String, output: String) {
    val parser = InfixToPostfixParser(InfixToPostfixLexer(input))
    val res = parser.e().res as String
    assertEquals(output, res)
  }
}