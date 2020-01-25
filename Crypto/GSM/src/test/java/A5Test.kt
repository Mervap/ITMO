import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class A5Test {
    @Test
    fun testA5() {
        val myKey = hexToByteArray("AA4B09182736CC67AA4B09182736CC69")
        val rand = hexToByteArray("9A32391B273ACC659A32391B273ACC65")
        val key = A3A8.a8(rand, myKey)
        val message = "ABF7298DC8728392378EF92788ABCABF7226BDE7623ECD738822788AB8"
        val encoded = A5.encode(key, 0, BigInteger(message, 16))
        val decoded = A5.encode(key, 0, encoded)
        println("Message: $message")
        println("Encoded: ${encoded.toString(16).toUpperCase()}")
        println("Decoded: ${decoded.toString(16).toUpperCase()}")
        assertEquals(message, decoded.toString(16).toUpperCase())
    }

    private fun hexToByteArray(hex: String): IntArray {
        return BigInteger(hex, 16).toByteArray().map { it.toInt() and 0xff }.dropWhile { it == 0 }.toIntArray()
    }
}