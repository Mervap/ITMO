
@ExperimentalStdlibApi
class DESEncoderDecoder(key: Long) {
    private val keys: LongArray = Utils.makeKeys(key)

    private fun base(data: ByteArray, keys: LongArray): ByteArray {
        assert(data.size % 8 == 0) { "Incorrect data size" }

        val dataSize = data.size
        val result = ByteArray(dataSize)
        for (i in 0 until dataSize / 8) {
            var roundData: Long = 0
            for (j in 0 until 8) {
                val index = i * 8 + j
                roundData = roundData.shl(8)
                roundData += if (data[index] < 0) {
                    Byte.MAX_VALUE.toLong() + (data[index] - Byte.MIN_VALUE) + 1
                }
                else {
                    data[index].toLong()
                }
            }

            val encodedData = Utils.transformBlock(roundData, keys)
            for (j in 0..7) {
                result[i * 8 + j] = encodedData.ushr((8 - j - 1) * 8).toByte()
            }
        }

        return result
    }
    
    fun encode(data: ByteArray): ByteArray {
        return base(data, keys)
    }

    fun decode(data: ByteArray): ByteArray {
        return base(data, keys.reversed().toLongArray())
    }
}