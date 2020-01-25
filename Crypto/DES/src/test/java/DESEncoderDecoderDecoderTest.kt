import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigInteger
import java.util.*

@ExperimentalStdlibApi
class DESEncoderDecoderDecoderTest {

    @Test
    fun testEncode() {
        val key = BigInteger("AABB09182736CCDD", 16).toLong()
        val en = DESEncoderDecoder(key)
        val st = BigInteger("123456ABCD132536", 16).toByteArray()
        val fn = BigInteger("C0B7A8D05F3A829C", 16).toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
        assertTrue(fn.contentEquals(en.encode(st)))
        assertTrue(st!!.contentEquals(en.decode(en.encode(st))))
    }

    @Test
    fun testPicture() {
        val key = BigInteger("33365F7193BEDBD1", 16).toLong()
        val encoder = DESEncoderDecoder(key)
        val inputData = File("pic").readBytes()
        val encodedData = encoder.encode(inputData)
        File("encodedPic").outputStream().write(encodedData)
        val decodedData = encoder.decode(encodedData)
        File("decodedPic").outputStream().write(decodedData)
        assertEquals(File("pic").readLines(), File("decodedPic").readLines())
    }

    private val data = Random().ints(10000000, 0, 26).toArray().map { 'a'.plus(it).toByte() }.toByteArray()

    @Test
    fun bench() {
        val key = BigInteger("33365F7193BEDBD1", 16).toLong()
        val encoder = DESEncoderDecoder(key)
        val inputData = data
        val encodedData = encoder.encode(inputData)
        val decodedData = encoder.decode(encodedData)
        assertTrue(decodedData.contentEquals(inputData))
    }
}