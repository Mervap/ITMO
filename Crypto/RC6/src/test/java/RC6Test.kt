import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.math.BigInteger
import java.util.*

@ExperimentalStdlibApi
class RC6Test {

    @Test
    fun testSubKeys() {
        val key = hexToByteArray("AABB09182736CCDD33365F7193BEDBD1")
        val answer = arrayOf(
                0x60f376bb, 0x28212f22, 0x5229e911, 0xeb86c555, 0x26100274, 0xcb5a0ea5, 0x5f3dd097, 0xf35ae6ca,
                0xd2f98793, 0xe7949bf2, 0x4f2b67c3, 0x5802ff85, 0xb597f9fd, 0xc2a5fd63, 0x4bc196fb, 0x3535e2be,
                0x4aecc1cd, 0xe443d1d4, 0x915b8a00, 0x6e476b61, 0x38d6051a, 0xd7cd6fbf, 0x9f50b764, 0xa225a8b8,
                0x71b38236, 0x8edab387, 0xccc2fdc0, 0x19fc5134, 0x71320a96, 0xdde09498, 0x42af0079, 0xdfe11127,
                0x62a4f3a1, 0xdbc9a99a, 0xa4285e7d, 0xe69cf1a6, 0x81b66fc4, 0x8f476a2a, 0x27e53bf2, 0x60972723,
                0x7011f079, 0x186e78c9, 0xfb551252, 0xf5054d6d).map { it.toInt() }.toIntArray()

        val subKeys = RC6(key).getSubKeys()
        assertTrue(subKeys.contentEquals(answer))
    }

    @Test
    fun testEncode() {
        val key = hexToByteArray("BDF931AB744DDBBAC9447AFEB52BD142")
        val answer = arrayOf(
                0x37, 0x17, 0x4f, 0xfe, 0xac, 0x91, 0xd6, 0x02,
                0x33, 0xdd, 0xbe, 0xe1, 0xe9, 0x24, 0xb5, 0x3b,
                0x13, 0x93, 0x8b, 0x5f, 0xf7, 0xed, 0x5d, 0xcb,
                0xb0, 0xdb, 0x4b, 0xcd, 0x26, 0x27, 0x00, 0x14).map { it.toByte() }.toByteArray()

        val encoded = RC6(key).encode("AB426FED83CA23AAB62129AADFE82847".toByteArray())
        assertTrue(encoded.contentEquals(answer))
    }

    @Test
    fun testDecode() {
        val key = hexToByteArray("BDF931AB744DDBBAC9447AFEB52BD142")
        val answer = arrayOf(
                0x19, 0xb3, 0x78, 0x81, 0xf4, 0x6a, 0x76, 0x67,
                0x61, 0x3b, 0xce, 0xa4, 0x6b, 0x47, 0x13, 0x79,
                0x5c, 0x97, 0x7d, 0x63, 0x12, 0x48, 0xb2, 0xd9,
                0x45, 0x1e, 0x7f, 0x09, 0x18, 0xe2, 0x4b, 0xfd).map { it.toByte() }.toByteArray()

        val decoded = RC6(key).decode("AB426FED83CA23AAB62129AADFE82847".toByteArray())
        assertTrue(decoded.contentEquals(answer))
    }

    @Test
    fun testEncodeDecode() {
        val key = hexToByteArray("ABD53234289CDE62FEABD8442ADDB4AAABDAEFEF23472ABD")
        val rc6 = RC6(key)
        val data = "ABDEDA626ABEA5196BA84A2BAEEEFDB137723BADEEF55555".toByteArray()
        assertTrue(data.contentEquals(rc6.decode(rc6.encode(data))))
    }

    @Test
    fun testPicture() {
        val key = hexToByteArray("AABFD453FA253E4289CDE662FE4FBDD84742ADDB4AAAB6DAAEFEF234B72AB8BD")
        val rc6 = RC6(key)
        val inputData = File("pic").readBytes()
        val encodedData = rc6.encode(inputData)
        File("encodedPic").outputStream().write(encodedData)
        val decodedData = rc6.decode(encodedData)
        File("decodedPic").outputStream().write(decodedData)
        assertEquals(File("pic").readLines(), File("decodedPic").readLines())
    }

    private val data = Random().ints(10000000, 0, 26).toArray().map { 'a'.plus(it).toByte() }.toByteArray()

    @Test
    fun bench() {
        val key = hexToByteArray("AABFD453FA253E4289CDE662FE4FBDD84742ADDB4AAAB6DAAEFEF234B72AB8BD")
        val rc6 = RC6(key)
        val inputData = data
        val encodedData = rc6.encode(inputData)
        val decodedData = rc6.decode(encodedData)
        assertTrue(decodedData.contentEquals(inputData))
    }

    private fun hexToByteArray(hex: String): ByteArray {
        return BigInteger(hex, 16).toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
    }
}