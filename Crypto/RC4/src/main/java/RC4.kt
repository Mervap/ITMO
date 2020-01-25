import kotlin.experimental.xor

const val WORD_SIZE = 8
const val ARRAY_SIZE = 1 shl WORD_SIZE

class RC4(private val key: ByteArray) {

    private var generatorArray = Array(ARRAY_SIZE) { it }
    private var x = 0
    private var y = 0

    init {
        reset()
    }

    fun reset() {
        x = 0
        y = 0

        generatorArray = generatorArray.mapIndexed { ind, _ -> ind }.toTypedArray()
        val keyLen = key.size
        var j = 0
        for (i in 0 until ARRAY_SIZE) {
            j = (j + generatorArray[i] + key[i % keyLen]) % ARRAY_SIZE
            generatorArray.swap(i, j)
        }
    }

    fun encode(message: ByteArray): ByteArray {
        val result = mutableListOf<Byte>()
        for (elem in message) {
            result.add(elem xor nextSequenceWord())
        }
        return result.toByteArray()
    }

    fun decode(encodedMessage: ByteArray): ByteArray {
        return encode(encodedMessage)
    }

    private fun nextSequenceWord(): Byte {
        x = (x + 1) % ARRAY_SIZE
        y = (y + generatorArray[x]) % ARRAY_SIZE
        generatorArray.swap(x, y)
        return generatorArray[(generatorArray[x] + generatorArray[y]) % ARRAY_SIZE].toByte()
    }

    companion object {
        private fun <T> Array<T>.swap(i: Int, j: Int) {
            val tmp = get(i)
            set(i, get(j))
            set(j, tmp)
        }
    }
}