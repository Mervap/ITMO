package keccak

object Keccak {

  fun getHash(inputMessage: ByteArray): ByteArray {
    val state = IntArray(200)
    val message: IntArray = inputMessage.map { it.toInt() }.toIntArray()
    val rateInBytes = RATE / Byte.SIZE_BITS

    // Absorbing
    for (i in 0 until (message.size / rateInBytes)) {
      for (j in 0 until rateInBytes) {
        state[j] = state[j] xor message[j + i * rateInBytes]
      }
      keccakF(state)
    }

    val lastBlockSize = message.size % rateInBytes
    if (lastBlockSize != 0) {
      for (j in 0 until lastBlockSize) {
        state[j] = state[j] xor message[j + (message.size / rateInBytes) * rateInBytes]
      }
    }

    // Padding
    state[lastBlockSize] = state[lastBlockSize] xor D
    if (lastBlockSize == rateInBytes - 1) {
      keccakF(state)
    }

    state[rateInBytes - 1] = state[rateInBytes - 1] xor 128
    keccakF(state)

    // Squeezing
    val outputLenInBytes = OUTPUT_LEN / Byte.SIZE_BITS
    return state.copyOfRange(0, outputLenInBytes).map { it.toByte() }.toByteArray()
  }

  private fun keccakF(inState: IntArray) {
    val keccakState = Array(5) { LongArray(5) }

    // From bits to longs
    for (i in 0..4) {
      for (j in 0..4) {
        val startOffset = 8 * (i + 5 * j)
        keccakState[i][j] = bitsToLong(inState.copyOfRange(startOffset, startOffset + 8))
      }
    }

    permutation(keccakState)

    // From longs to bits
    for (i in 0..4) {
      for (j in 0..4) {
        val startOffset = 8 * (i + 5 * j)
        longToBits(keccakState[i][j]).copyInto(inState, startOffset)
      }
    }
  }

  private fun permutation(state: Array<LongArray>) {
    for (round in 0..23) {
      val c = LongArray(5)
      val d = LongArray(5)

      // θ step
      for (i in 0..4) {
        c[i] = state[i][0] xor state[i][1] xor state[i][2] xor state[i][3] xor state[i][4]
      }

      for (i in 0..4) {
        d[i] = c[(i + 4) % 5] xor leftRotate64(c[(i + 1) % 5], 1)
      }

      for (i in 0..4) {
        for (j in 0..4) {
          state[i][j] = state[i][j] xor d[i]
        }
      }

      // ρ and π steps
      var x = 1
      var y = 0
      var current = state[x][y]
      for (i in 0..23) {
        val tmp = x
        x = y
        y = (2 * tmp + 3 * y) % 5
        val shiftValue = current
        current = state[x][y]
        state[x][y] = leftRotate64(shiftValue, (i + 1) * (i + 2) / 2)
      }

      // χ step
      for (j in 0..4) {
        val t = LongArray(5)
        for (i in 0..4) {
          t[i] = state[i][j]
        }
        for (i in 0..4) {
          val invertVal = t[(i + 1) % 5].inv()
          state[i][j] = t[i] xor (invertVal and t[(i + 2) % 5])
        }
      }

      // ι step
      state[0][0] = state[0][0] xor RC[round]
    }
  }

  // SHA3-512 params
  private const val RATE = 576
  private const val D = 0x06
  private const val OUTPUT_LEN = 512

  private val RC = arrayOf(
    0x0000000000000001, 0x0000000000008082, -0x7fffffffffff7f76, -0x7fffffff7fff8000,
    0x000000000000808B, 0x0000000080000001, -0x7fffffff7fff7f7f, -0x7fffffffffff7ff7,
    0x000000000000008A, 0x0000000000000088, 0x0000000080008009, 0x000000008000000A,
    0x000000008000808B, -0x7fffffffffffff75, -0x7fffffffffff7f77, -0x7fffffffffff7ffd,
    -0x7fffffffffff7ffe, -0x7fffffffffffff80, 0x000000000000800A, -0x7fffffff7ffffff6,
    -0x7fffffff7fff7f7f, -0x7fffffffffff7f80, 0x0000000080000001, -0x7fffffff7fff7ff8
  )

  private fun bitsToLong(bits: IntArray): Long {
    var long = 0L
    for (i in 0..7) {
      long = long or (bits[i].toLong() shl (8 * i))
    }
    return long
  }

  private fun longToBits(long: Long): IntArray {
    val bits = IntArray(8)
    for (i in 0..7) {
      bits[i] = ((long ushr (8 * i)) % 256).toInt()
    }
    return bits
  }

  private fun leftRotate64(value: Long, rotate: Int): Long {
    val l = value ushr (64 - rotate % 64)
    val r = value shl (rotate % 64)
    return l or r
  }
}