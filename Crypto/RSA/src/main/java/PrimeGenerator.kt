import java.math.BigInteger
import java.util.*

class PrimeGenerator {

  private val primeSeq = arrayOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41).map { it.toBigInteger() }

  private fun getTwo(n: BigInteger): Int {
    var cur = n
    var a = 0
    while (cur % TWO == ZERO) {
      cur = cur shr 1
      ++a
    }

    return a
  }

  private fun millerRabin(n: BigInteger): Boolean {
    if (n == TWO) return true
    if (n < TWO || n % TWO == ZERO) return false

    val s = getTwo(n - ONE)
    val t = (n - ONE) shr s

    for (e in primeSeq) {
      if (e >= n) break
      var x = Utils.binMul(e, t, n)
      if (x == ONE || x == n - ONE) continue

      var i = 0
      while (i < s - 1) {
        x = (x * x) % n
        if (x == ONE) return false
        if (x == n - ONE) break
        ++i
      }

      if (i == s - 1) return false
    }
    return true
  }

  fun probablePrime(): BigInteger {
    var result = BigInteger(1024, Random())
    while (!millerRabin(result)) {
      result = BigInteger(1024, Random())
    }
    return result
  }

  companion object {
    private val ZERO = BigInteger.ZERO
    private val ONE = BigInteger.ONE
    private val TWO = BigInteger.TWO
  }
}