import kotlin.math.max

private const val W = 32
private const val R = 20
private const val P32 = 0xb7e15163
private const val Q32 = 0x9e3779b9

class RC6(key: ByteArray) {

    private val subKeys: IntArray

    init {
        val keyLen = key.size
        assert(keyLen in listOf(16, 24, 32)) { "The length of the key must be 16, 24 or 32. Actual: $keyLen" }

        val c = keyLen / 4
        val littleEndian = key.toLittleEndianIntArray()

        val subKeyssCount = 2 * R + 4
        subKeys = IntArray(subKeyssCount)
        subKeys[0] = P32.toInt()
        for (i in 1 until subKeyssCount) {
            subKeys[i] = (subKeys[i - 1] + Q32).toInt()
        }

        var a = 0
        var b = 0
        var i = 0
        var j = 0
        val v = 3 * max(c, subKeyssCount)
        for (q in 0 until v) {
            subKeys[i] = (subKeys[i] + a + b).cycleShl(3)
            a = subKeys[i]
            littleEndian[j] = (littleEndian[j] + a + b).cycleShl(a + b)
            b = littleEndian[j]
            i = (i + 1) % subKeyssCount
            j = (j + 1) % c
        }
    }

    private fun Int.cycleShl(count: Int): Int {
        val realCount = count % W
        return shl(realCount) or ushr(W - realCount)
    }

    private fun Int.cycleShr(count: Int): Int {
        return cycleShl(W - count)
    }

    private fun ByteArray.toLittleEndianIntArray(): IntArray {
        val c = size / 4
        val littleEndian = mutableListOf<Int>()
        for (i in 0 until c) {
            val j = i * 4
            littleEndian.add(get(j).toInt() and 0xFF
                    or (get(j + 1).toInt() and 0xFF shl 8)
                    or (get(j + 2).toInt() and 0xFF shl 16)
                    or (get(j + 3).toInt() and 0xFF shl 24))
        }

        return littleEndian.toIntArray()
    }

    private fun Int.toLittleEndianBytes(): Array<Byte> {
        val res = Array<Byte>(4) { 0 }
        for (i in 0..3) {
            res[i] = (ushr(i * 8) and 0xff).toByte()
        }
        return res
    }

    fun getSubKeys(): IntArray {
        return subKeys.copyOf()
    }

    fun encode(input: ByteArray): ByteArray {
        val inputSize = input.size
        assert(inputSize % 16 == 0) { "The length of the input data must be a multiple of 16. Actual: $inputSize" }

        val littleEndian = input.toLittleEndianIntArray()
        val result = mutableListOf<Byte>()
        for (i in 0 until littleEndian.size / 4) {
            val j = i * 4
            var a = littleEndian[j]
            var b = littleEndian[j + 1]
            var c = littleEndian[j + 2]
            var d = littleEndian[j + 3]

            b += subKeys[0]
            d += subKeys[1]
            for (k in 1..R) {
                val modifiedB = (b * (2 * b + 1)).cycleShl(5)
                val modifiedD = (d * (2 * d + 1)).cycleShl(5)
                a = (a xor modifiedB).cycleShl(modifiedD) + subKeys[2 * k]
                c = (c xor modifiedD).cycleShl(modifiedB) + subKeys[2 * k + 1]

                // (A, B, C, D) = (B, C, D, A)
                val tmp = a
                a = b
                b = c
                c = d
                d = tmp
            }
            a += subKeys[2 * R + 2]
            c += subKeys[2 * R + 3]

            result.addAll(a.toLittleEndianBytes())
            result.addAll(b.toLittleEndianBytes())
            result.addAll(c.toLittleEndianBytes())
            result.addAll(d.toLittleEndianBytes())
        }

        return result.toByteArray()
    }

    fun decode(input: ByteArray): ByteArray {
        val inputSize = input.size
        assert(inputSize % 16 == 0) { "The length of the input data must be a multiple of 16. Actual: $inputSize" }

        val littleEndian = input.toLittleEndianIntArray()
        val result = mutableListOf<Byte>()
        for (i in 0 until littleEndian.size / 4) {
            val j = i * 4
            var a = littleEndian[j]
            var b = littleEndian[j + 1]
            var c = littleEndian[j + 2]
            var d = littleEndian[j + 3]

            c -= subKeys[2 * R + 3]
            a -= subKeys[2 * R + 2]
            for (k in R downTo 1) {
                // (A, B, C, D) = (D, A, B, C)
                val tmp = d
                d = c
                c = b
                b = a
                a = tmp

                val modifiedD = (d * (2 * d + 1)).cycleShl(5)
                val modifiedB = (b * (2 * b + 1)).cycleShl(5)
                c = (c - subKeys[2 * k + 1]).cycleShr(modifiedB) xor modifiedD
                a = (a - subKeys[2 * k]).cycleShr(modifiedD) xor modifiedB
            }
            d -= subKeys[1]
            b -= subKeys[0]

            result.addAll(a.toLittleEndianBytes())
            result.addAll(b.toLittleEndianBytes())
            result.addAll(c.toLittleEndianBytes())
            result.addAll(d.toLittleEndianBytes())
        }

        return result.toByteArray()
    }
}