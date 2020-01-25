import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.Long.parseLong
import java.lang.RuntimeException
import java.math.BigInteger

@ExperimentalStdlibApi
class UtilsTest {

    @Test
    fun testPermutation() {
        var bits = 1L
        for (cnt in 0..63) {
            assertEquals(bits, Utils.permuteData(Utils.permuteData(bits, 64, DESData.IP), 64, DESData.REVERSED_IP))
            bits *= 2
        }

        bits = 4996035723270197587
        assertEquals(bits, Utils.permuteData(Utils.permuteData(bits, 64, DESData.IP), 64, DESData.REVERSED_IP))
    }

    @Test
    fun sBox() {
        var sixBits = 0x2AL
        assertEquals(6, Utils.blockConvert(sixBits, 0))

        sixBits = 0x2DL
        assertEquals(13, Utils.blockConvert(sixBits, 3))

        sixBits = 0x1FL
        assertEquals(6, Utils.blockConvert(sixBits, 4))
    }

    @Test
    fun testKeyGen() {
        val key = BigInteger("AABB09182736CCDD", 16).toLong()
        val subKeys = arrayOf(
                "194CD072DE8C", "4568581ABCCE", "06EDA4ACF5B5", "DA2D032B6EE3",
                "69A629FEC913", "C1948E87475E", "708AD2DDB3C0", "34F822F0C66D",
                "84BB4473DCCC", "02765708B5BF", "6D5560AF7CA5", "C2C1E96A4BF3",
                "99C31397C91F", "251B8BC717D0", "3330C5D9A36D", "181C5D75C66D")
        assertTrue(Utils.makeKeys(key).contentEquals(subKeys.map { parseLong(it, 16) }.toLongArray()))
        assertThrows(RuntimeException::class.java) { Utils.makeKeys(key - 1) }
    }

    @Test
    fun testRounds() {
        val key = BigInteger("AABB09182736CCDD", 16).toLong()
        val keys = Utils.makeKeys(key)
        val ls = arrayOf(
                "18CA18AD", "5A78E394", "4A1210F6", "B8089591",
                "236779C2", "A15A4B87", "2E8F9C65", "A9FC20A3",
                "308BEE97", "10AF9D37", "6CA6CB20", "FF3C485F",
                "22A5963B", "387CCDAA", "BD2DD2AB", "CF26B472")
        val rs = arrayOf(
                "5A78E394", "4A1210F6", "B8089591", "236779C2",
                "A15A4B87", "2E8F9C65", "A9FC20A3", "308BEE97",
                "10AF9D37", "6CA6CB20", "FF3C485F", "22A5963B",
                "387CCDAA", "BD2DD2AB", "CF26B472", "19BA9212")

        var l = parseLong("14A7D678", 16)
        var r = parseLong("18CA18AD", 16)
        for (i in 0 until 16) {
            val newL = r
            r = l.xor(Utils.round(r, keys[i]))
            l = newL
            assertEquals(l, parseLong(ls[i], 16))
            assertEquals(r, parseLong(rs[i], 16))
        }
    }
}