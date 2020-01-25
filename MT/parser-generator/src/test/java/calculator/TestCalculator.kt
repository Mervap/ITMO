package calculator

import org.junit.Test
import kotlin.test.assertEquals

class TestCalculator {

  @Test
  fun testMinus() {
    doTest("2 - 2 * (2 - 10)", 18)
  }

  @Test
  fun testCommon() {
    doTest("2 + (2 + 2) * (2 + 2 - 3)", 6)
    doTest("3 + (2 + 2) * (2 + 2 - 3)", 7)
    doTest("10 + 20 * 10 - (12 + 1)", 197)
  }

  @Test
  fun testPriority() {
    doTest("2 + 2 * 2", 6)
    doTest("2 * 2 + 2", 6)
    doTest("20 - 10 - 15 + 1", -4)
    doTest("(20 - 10 - 15 + 1) / -2", 2)
    doTest("11 + -2", 9)
    doTest("11 -- 11", 22)
    doTest("-(10 + 15)", -25)
    doTest("-11 -- 11", 0)
  }

  @Test
  fun testBraces() {
    doTest("10 + (((15 - (((42 +  ((-10))))) + 1)))", -6)
  }

  @Test
  fun testWhitespace() {
    val t = "\t"
    doTest("""
       $t    $t 10 + 
       15 $t $t 
       - 43 +  
       $t $t 
       
       
                           12
    """, -6)
  }

  @Test
  fun testAS() {
    doTest("1 - 2 - 2 * 2", -5)
  }

  private fun doTest(input: String, output: Int) {
    val parser = CalculatorParser(CalculatorLexer(input))
    val res = parser.e().res as Int
    assertEquals(output, res)
  }
}
