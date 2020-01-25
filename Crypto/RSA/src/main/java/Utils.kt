import java.math.BigInteger

object Utils {

  fun binMul(a: BigInteger, n: BigInteger, mod: BigInteger): BigInteger {
    if (n == ZERO) return ONE
    if (n == ONE) return a
    return if (n % TWO == ZERO) {
      val t = binMul(a, n shr 1, mod)
      (t * t) % mod
    } else {
      val t = binMul(a, n - ONE, mod)
      (t * a) % mod
    }
  }

  private val ZERO = BigInteger.ZERO
  private val ONE = BigInteger.ONE
  private val TWO = BigInteger.TWO
}