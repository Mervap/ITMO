object A3A8 {
    fun a3(rand: IntArray, key: IntArray): Long {
        return base(rand, key).slice(0..3).map { it.toLong() }.reduce { acc, v -> (acc shl 8) or v }
    }

    fun a8(rand: IntArray, key: IntArray): Long {
        return base(rand, key).slice(4..11).map { it.toLong() }.reduce { acc, v -> (acc shl 8) or v }
    }

    private fun base(rand: IntArray, key: IntArray): IntArray {
        val x = IntArray(32) { 0 }
        val bit = IntArray(128) { 0 }

        for (i in 16..31) {
            x[i] = rand[i - 16]
        }
        for (i in 0..7) {
            for (j in 0..15) {
                x[j] = key[j]
            }
            for (j in 0..4) {
                for (k in 0 until (1 shl j)) {
                    for (l in 0 until (1 shl (4 - j))) {
                        val m = l + k * (1 shl (5 - j))
                        val n = m + (1 shl (4 - j))

                        val y = (x[m] + 2 * x[n]) % (1 shl (9 - j))
                        val z = (2 * x[m] + x[n]) % (1 shl (9 - j))
                        x[m] = Data.Tables[j][y]
                        x[n] = Data.Tables[j][z]
                    }
                }
            }
            for (j in 0..31) {
                for (k in 0..3) {
                    bit[4 * j + k] = (x[j] shr (3 - k)) and 1
                }
            }
            if (i < 7) {
                for (j in 0..15) {
                    x[j + 16] = 0
                    for (k in 0..7) {
                        val nextBit = ((8 * j + k) * 17) % 128
                        x[j + 16] = x[j + 16] or ((bit[nextBit] shl (7 - k)) and 0xff)
                    }
                }
            }
        }

        val result = IntArray(12) { 0 }
        for (i in 0..3) {
            result[i] = (x[2 * i] shl 4) and 0xff or x[2 * i + 1]
        }
        for (i in 0..5) {
            result[4 + i] = ((x[2 * i + 18] shl 6) and 0xff or ((x[2 * i + 18 + 1] shl 2) and 0xff) or (x[2 * i + 18 + 2] shr 2) and 0xff)
        }
        result[4 + 6] = (x[2 * 6 + 18] shl 6) and 0xff or ((x[2 * 6 + 18 + 1] shl 2) and 0xff)
        result[4 + 7] = 0
        return result
    }
}
