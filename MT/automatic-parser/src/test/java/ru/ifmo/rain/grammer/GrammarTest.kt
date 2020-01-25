package ru.ifmo.rain.grammer

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.junit.Test
import ru.ifmo.rain.grammar.ArithmeticLexer
import ru.ifmo.rain.grammar.ArithmeticParser
import kotlin.test.assertEquals

class GrammarTest {

  @Test
  fun testTask() {
    doTest("""
      a = 2;
      b = a + 2;
      c = a + b * (b - 3);
      a = 3;
      c = a + b * (b - 3);
    """.trimIndent(), """
      a = 2
      b = 4
      c = 6
      a = 3
      c = 7
    """.trimIndent())
  }

  @Test
  fun testAssignmentAsExpression() {
    doTest("""
      a = b = c = d = 10 + 20 * (e = 10) - l = 12 + 1;
    """.trimIndent(), """
      e = 10
      l = 13
      d = 197
      c = 197
      b = 197
      a = 197
    """.trimIndent())
  }

  @Test
  fun testPriority() {
    doTest("""
      a = 2 + 2 * 2;
      b = 2 * 2 + 2;
      c = 2 * b = 10 + 15;
      d = 20 - 10 - 15 + 1;
      e = d / -2;
      f = 11 + -2;
      g = 11 -- 11;
      r = -(10 + 15);
      ll = - g = 10;
    """.trimIndent(), """
      a = 6
      b = 6
      b = 25
      c = 50
      d = -4
      e = 2
      f = 9
      g = 22
      r = -25
      g = 10
      ll = -10
    """.trimIndent())
  }

  @Test
  fun testComplicatedName() {
    doTest("""
      a_ba_ca_ba_daBAaa = 15;
      a_16 = 10 + __A = 10;
    """.trimIndent(), """
      a_ba_ca_ba_daBAaa = 15
      __A = 10
      a_16 = 20
    """.trimIndent())
  }

  @Test
  fun testNumberBoundary() {
    doTest("""
      a = ${Int.MAX_VALUE};
      b = ${Int.MIN_VALUE};
      c = a + b;
      d = ${Int.MAX_VALUE} + ${Int.MIN_VALUE};
    """.trimIndent(), """
      a = ${Int.MAX_VALUE}
      b = ${Int.MIN_VALUE}
      c = -1
      d = -1
    """.trimIndent())
  }

  @Test
  fun testBraces() {
    doTest("""
      a = 10 + (((15 - (((42 + b = ((-10))))) + 1)));
    """.trimIndent(), """
      b = -10
      a = -6
    """.trimIndent())
  }

  @Test
  fun testWhitespaces() {
    val t = "\t"
    doTest("""
      a = $t    $t 10 + 
       15 $t $t 
       - 43 + b = 
       $t $t 
       
       
                           12;
    """.trimIndent(), """
      b = 12
      a = -6
    """.trimIndent())
  }

  @Test
  fun testPow() {
    doTest("""
      a = 10;
      b = 5;
      c = a ** b;
    """.trimIndent(), """
      a = 10
      b = 5
      c = 100000
    """.trimIndent())
  }

  @Test
  fun testPowRight() {
    doTest("""
      a = 2 ** 2 ** 3;
    """.trimIndent(), """
      a = 256
    """.trimIndent())
  }

  private fun doTest(input: String, expected: String) {
    val inputStream = CharStreams.fromString(input)
    val lexer = ArithmeticLexer(inputStream)
    val parser = ArithmeticParser(CommonTokenStream(lexer))
    parser.root()
    assertEquals(expected, parser.allAssignments.joinToString("\n") { "${it.key} = ${it.value}" })
  }
}