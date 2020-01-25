import java.io.File
import java.io.ObjectInputStream
import java.math.BigInteger
import kotlin.random.Random


class RSA {
  private val primes: List<BigInteger>

  init {
    val inputStream = ObjectInputStream(File("primes.db").inputStream())
    primes = inputStream.readObject() as List<BigInteger>
  }

  private fun evkl(a: BigInteger, b:BigInteger): Triple<BigInteger, BigInteger, BigInteger> {
    if (a == ZERO) return Triple(b, ZERO, ONE)
    val (d, x, y) = evkl(b % a, a)
    return Triple(d, y - (b / a) * x, x)
  }

  private fun modInverse(a: BigInteger, mod: BigInteger): BigInteger {
    val (_, x, _) = evkl(a, mod)
    return (x + mod) % mod
  }

  fun getKeys(): Pair<Pair<BigInteger, BigInteger>, Pair<BigInteger, BigInteger>> {
    val pi = Random.nextInt(primes.size)
    val qi = Random.nextInt(primes.size)
    if (pi == qi) getKeys()

    val p = primes[pi]
    val q = primes[qi]
    val mod = p * q
    val phi = (p - ONE) * (q - ONE)

    var ei = Random.nextInt(primes.size)
    var e = primes[ei]
    while (ei == pi || ei == qi || e == p || e == q || e >= phi) {
      ei = Random.nextInt(primes.size)
      e = primes[ei]
    }

    val d = modInverse(e, phi)
    assert(e * d % phi == ONE)
    return ((e to mod) to (d to mod))
  }

  companion object {

    fun encrypt(message: BigInteger, publicKey: Pair<BigInteger, BigInteger>): BigInteger {
      return Utils.binMul(message, publicKey.first, publicKey.second)
    }

    fun decrypt(encrypted: BigInteger, privateKey: Pair<BigInteger, BigInteger>): BigInteger {
      return Utils.binMul(encrypted, privateKey.first, privateKey.second)
    }

    private val ZERO = BigInteger.ZERO
    private val ONE = BigInteger.ONE
  }
}