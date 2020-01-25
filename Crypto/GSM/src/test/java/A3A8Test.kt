import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

@ExperimentalStdlibApi
class A3A8Test {

    @Test
    fun test() {
        val key = hexToByteArray("AA4B09182736CC67AA4B09182736CC69")
        val rand = hexToByteArray("9A32391B273ACC659A32391B273ACC65")
        assertEquals("F918BC37", A3A8.a3(rand, key).toString(16).toUpperCase())
        assertEquals("5AFD9666C16A0000", A3A8.a8(rand, key).toString(16).toUpperCase())
    }

    private fun hexToByteArray(hex: String): IntArray {
        return BigInteger(hex, 16).toByteArray().map { it.toInt() and 0xff }.dropWhile { it == 0 }.toIntArray()
    }
}