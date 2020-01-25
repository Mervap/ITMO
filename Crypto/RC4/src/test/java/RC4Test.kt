import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigInteger
import java.util.*

@ExperimentalStdlibApi
class RC4Test {

    @Test
    fun testSmallText() {
        val key = hexToByteArray("0A0B091827360C0D33365F01030E0B01")
        val rc4 = RC4(key)
        val text = "This is the test".toByteArray()
        val encoded = rc4.encode(text)
        rc4.reset()
        val decoded = rc4.decode(encoded)
        assertTrue(text.contentEquals(decoded))
    }

    @Test
    fun testLongText() {
        val key = hexToByteArray("0A0B091827360C0D33365F01030E0B01")
        val rc4 = RC4(key)
        val inputData = File("input").readBytes()
        val encodedData = rc4.encode(inputData)
        File("encoded").outputStream().write(encodedData)
        rc4.reset()
        val decodedData = rc4.decode(encodedData)
        File("decoded").outputStream().write(decodedData)
        assertEquals(File("input").readLines(), File("decoded").readLines())
    }

    private val data = Random().ints(10000000, 0, 26).toArray().map { 'a'.plus(it).toByte() }.toByteArray()

    @Test
    fun bench() {
        val key = hexToByteArray("0A0B091827360C0D33365F01030E0B01")
        val rc4 = RC4(key)
        val inputData = data
        val encodedData = rc4.encode(inputData)
        rc4.reset()
        val decodedData = rc4.decode(encodedData)
        assertTrue(decodedData.contentEquals(inputData))
    }

    private fun hexToByteArray(hex: String): ByteArray {
        return BigInteger(hex, 16).toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
    }
}