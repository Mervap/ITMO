import java.lang.RuntimeException

@ExperimentalStdlibApi
object Utils {

    fun makeKeys(key: Long): LongArray {
        val fullByte = 0xFFL
        for (i in 0..7) {
            val byteCnt = (8 - i - 1) * 8
            val byte = key.and(fullByte.shl(byteCnt)).ushr(byteCnt)
            if (byte.countOneBits() % 2 == 1) {
                throw RuntimeException("Bad key")
            }
        }

        val c = DESData.C.toList()
        val d = DESData.D.toList()

        var l = permuteData(key, Long.SIZE_BITS, c.toTypedArray())
        var r = permuteData(key, Long.SIZE_BITS, d.toTypedArray())

        val res = LongArray(16)
        for (i in 0 until 16) {
            for (j in 0 until DESData.SHIFT[i]) {
                val bit = l.and(1L.shl(27)).ushr(27)
                l = l.xor(bit.shl(27)).shl(1) + bit

                val bitr = r.and(1L.shl(27)).ushr(27)
                r = r.xor(bitr.shl(27)).shl(1) + bitr
            }
            res[i] = permuteData(l.shl(56 / 2) + r, 56, DESData.KEY_PERM)
        }

        return res
    }

    fun permuteData(data: Long, dataLen: Int, permutation: Array<Int>): Long {
        var rearrangedData = 0L
        for (i in permutation.indices) {
            val bitNumber = dataLen - permutation[i]
            rearrangedData += data.and(1L.shl(bitNumber)).ushr(bitNumber).shl(permutation.size - i - 1)
        }

        return rearrangedData
    }

    fun blockConvert(data: Long, sBoxNumber: Int): Long {
        val row = data.and(1.shl(5)).toInt().ushr(4) + data.and(1).toInt()
        val column = data.and(0xF.shl(1)).toInt().ushr(1)
        return DESData.S_BOXS[sBoxNumber][row * 16 + column].toLong()
    }

    fun round(data: Long, key: Long): Long {
        val extendedData = permuteData(data, 32, DESData.E).xor(key)
        var resultData = 0L
        val sixBits: Long = 0x3F
        for (i in 0 until 8) {
            val bitNumber = (8 - i - 1) * 6
            resultData += blockConvert(extendedData.and(sixBits.shl(bitNumber)).ushr(bitNumber), i).toLong().shl((8 - i - 1) * 4)
        }

        return permuteData(resultData, 32, DESData.P)
    }

    fun transformBlock(data: Long, keys: LongArray): Long {
        val rearrangedData = permuteData(data, Long.SIZE_BITS, DESData.IP)

        var l = rearrangedData.ushr(Long.SIZE_BITS / 2)
        var r = rearrangedData.and(0xFFFFFFFFL)
        for (key in keys) {
            val newL = r
            r = l.xor(round(r, key))
            l = newL
        }

        return permuteData(r.shl(Long.SIZE_BITS / 2) + l, Long.SIZE_BITS, DESData.REVERSED_IP)
    }
}