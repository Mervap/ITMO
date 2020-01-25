import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*
import java.util.concurrent.Phaser
import kotlin.concurrent.thread

class RSATests {

  // 44 ms
  @Test
  fun testEncryptDecrypt() {
    val rsa = RSA()
    for (i in 0 until 5) {
      val (publicKey, privateKey) = rsa.getKeys()
      println("Public key: " + publicKey.first.toString(16).toUpperCase())
      println("Private key: " + privateKey.first.toString(16).toUpperCase())
      println("Modulus: " + publicKey.second.toString(16).toUpperCase())
      println()
      val message = if (i == 0) {
        BigInteger("AB18412919BBDACD810832923BFE95FD", 16)
      }
      else {
        BigInteger(512, Random())
      }
      val encrypted = RSA.encrypt(message, publicKey)
      val decrypted = RSA.decrypt(encrypted, privateKey)
      assertEquals(message, decrypted)
    }
  }


  @Test
  fun testProbablePrime() {
    val threads = Runtime.getRuntime().availableProcessors()
    println(threads)

    val onFinish = Phaser(threads + 1)
//      val res = CopyOnWriteArrayList<BigInteger>()
    for (i in 1..threads) {
      thread {
        println("Thread $i: ${Thread.currentThread().id}")
        val p = PrimeGenerator()
        for (j in 0..3) {
//            val np = BigInteger.probablePrime(1024, Random())
          val np = p.probablePrime()
//            res.add(np)
          assertTrue(np.isProbablePrime(1000), np.toString())
        }
        onFinish.arrive()
      }
    }
    onFinish.arriveAndAwaitAdvance()
//      File("res").writeText(res.joinToString("\n"))
  }

}