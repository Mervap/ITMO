import keccak.Keccak
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class KeccakTest {

  @Test
  fun testCommon() {
    assertEquals(convertBytesToString(Keccak.getHash("".toByteArray())),
      "A69F73CCA23A9AC5C8B567DC185A756E97C982164FE25859E0D1DCC1475C80A615B2123AF1F5F94C11E3E9402C3AC558F500199D95B6D3E301758586281DCD26")

    assertEquals(convertBytesToString(Keccak.getHash("The quick brown fox jumps over the lazy dog".toByteArray())),
      "01DEDD5DE4EF14642445BA5F5B97C15E47B9AD931326E4B0727CD94CEFC44FFF23F07BF543139939B49128CAF436DC1BDEE54FCB24023A08D9403F9B4BF0D450")

    assertEquals(convertBytesToString(Keccak.getHash("The quick brown fox jumps over the lazy dog.".toByteArray())),
      "18F4F4BD419603F95538837003D9D254C26C23765565162247483F65C50303597BC9CE4D289F21D1C2F1F458828E33DC442100331B35E7EB031B5D38BA6460F8")
  }

  @Test
  fun testPerformance() {
    val input = mutableListOf<Byte>()
    val size = 10_000_000
    repeat(size) { input.add(Random().nextInt().toByte()) }

    val st = Date().time
    Keccak.getHash(input.toByteArray())
    val fn = Date().time
    print("Size: $size\nTime: ${fn - st}ms\n")
  }

  private fun convertBytesToString(data: ByteArray): String {
    return buildString {
      for (i in data.indices) {
        val uVal = data[i].toInt() and 0xFF
        append(intToHexChar(uVal ushr 4))
        append(intToHexChar(uVal and 0xF))
      }
    }
  }

  private fun intToHexChar(i: Int): Char {
    return when (i) {
      in 0..9 -> i.toString()[0].toUpperCase()
      10 -> 'A'
      11 -> 'B'
      12 -> 'C'
      13 -> 'D'
      14 -> 'E'
      15 -> 'F'
      else -> error("")
    }
  }
}