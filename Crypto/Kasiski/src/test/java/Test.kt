import org.junit.Test
import java.io.File
import java.lang.StringBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

const val ALF = "abcdefghijgklmopqrstuvwxyz"

class Test {

    @Test
    fun testPermutation() {
        val text = File("text").readLines().joinToString()
        for (i in 0..10000) {
            val keysCnt = (2..15).random()
            val keys = List(keysCnt) { ALF.toMutableList().also { it.shuffle() }.joinToString(separator = "") }
            val encoder = Encoder(keys)
            val encoded = encoder.encode(text)
            val kasiskiKeysCnts = Kasiski().hack(encoded)
            assertTrue(kasiskiKeysCnts.contains(keysCnt), "Stage: $i")
        }
    }

    @Test
    fun testVigenere() {
        val text = File("text").readLines().joinToString()
        for (i in 0..10000) {
            val keysCnt = (2..4).random()
            val realKey = StringBuilder()
            for (j in 0 until keysCnt) {
                realKey.append('a'.plus((0..25).random()))
            }
            val keys = List(keysCnt) {
                ALF.map { c -> 'a'.plus(c.minus('a').plus(realKey[it].minus('a')).rem(26)) }.joinToString(separator = "")
            }
            val encoder = Encoder(keys)
            val encoded = encoder.encode(text)
            val kasiskiKeysCnts = Kasiski().hack(encoded)
            assertTrue(kasiskiKeysCnts.contains(keysCnt), "Stage: $i")
            val hackKeys = kasiskiKeysCnts.map { Kasiski().vigenereFreq(encoded, it) }
            assertTrue(hackKeys.contains(realKey.toString()), realKey.toString())
        }
    }
}