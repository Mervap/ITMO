import java.math.BigInteger

private const val INIT_CLOCK = 100

object A5 {
    fun encode(key: Long, roundNum: Int, data: BigInteger): BigInteger {
        val regs = listOf(Register(0), Register(1), Register(2))
        init(regs, key, roundNum)
        var a = 0L.toBigInteger()
        for (i in 0..227) {
            a = (a shl 1) or nextBit(regs).toBigInteger()
        }
        return data xor a
    }

    private fun nextBit(regs: List<Register>): Int {
        val clock = clock(regs)
        regs.forEach { it.shr(clock) }
        return regs.map { it.lastBit() }.reduce { acc, v -> acc xor v }
    }

    private fun init(regs: List<Register>, key: Long, roundNum: Int) {
        for (i in 0..63) {
            for (j in 0..2) {
                regs.forEach { it.shr(INIT_CLOCK, it.nextBit() xor key.bit(i)) }
            }
        }

        for (i in 0..21) {
            regs.forEach { it.shr(INIT_CLOCK, it.nextBit() xor roundNum.bit(i)) }
        }

        for (i in 0..99) {
            val clock = clock(regs)
            regs.forEach { it.shr(clock) }
        }
    }

    private fun clock(regs: List<Register>): Int {
        val oneCnt = regs.map { it.clock() }.reduce { acc, v -> acc + v }
        return if (oneCnt >= 2) 1 else 0
    }

    private class Register(private val regNum: Int) {
        private var data: Int = 0

        fun lastBit(): Int {
            return bit(SIZE[regNum] - 1)
        }

        private fun bit(i: Int): Int {
            return if (data and (1 shl (SIZE[regNum] - i - 1)) > 0) 1 else 0
        }

        fun nextBit() = INPUT[regNum].map { bit(it) }.reduce { acc, v -> acc xor v }

        fun shr(clock: Int, nextBit: Int? = null) {
            if (clock != INIT_CLOCK && clock() != clock) return
            val newBit = nextBit ?: nextBit()
            data = (data ushr 1) or (newBit shl (SIZE[regNum] - 1))
        }

        fun clock(): Int {
            return bit(CLOCK[regNum])
        }

        companion object {
            val SIZE = listOf(19, 22, 23)
            private val CLOCK = listOf(8, 10, 10)
            private val INPUT = listOf(
                    listOf(13, 16, 17, 18),
                    listOf(20, 21),
                    listOf(7, 20, 21, 22))
        }
    }

    private fun Int.bit(i: Int): Int {
        return if (this and (1 shl i) > 0) 1 else 0
    }

    private fun Long.bit(i: Int): Int {
        return if (this and (1L shl i) > 0) 1 else 0
    }
}
