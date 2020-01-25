import kotlin.math.abs

const val P = 31

class Kasiski {
    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    fun vigenereFreq(inputText: String, keyLen: Int): String {
        val freq = List(keyLen) { MutableList(26) { 0 } }
        for (i in inputText.indices) {
            freq[i % keyLen][inputText[i].toLowerCase().minus('a')] = freq[i % keyLen][inputText[i].toLowerCase().minus('a')] + 1
        }
        val eTo = freq.map { it.mapIndexed { ind, cnt -> 'a'.plus(ind) to cnt }.sortedByDescending { it.second } }
        return eTo.map { it[0].first }.map { if (it < 'e') 'a'.plus('z'.minus('e').plus(it.minus('a') + 1)) else 'a'.plus(it.minus('e')) }.joinToString(separator = "")
    }

    fun hack(inputText: String): List<Int> {
        assert(inputText.length > 1000)
        val trigrams = mutableMapOf<Long, MutableList<Int>>()
        var hash = 0L
        for (i in 0..2) {
            hash *= P
            hash += inputText[i].toInt()
        }

        trigrams[hash] = mutableListOf(0)
        for (i in 3 until inputText.length) {
            hash -= inputText[i - 3].toInt() * P * P
            hash *= P
            hash += inputText[i].toInt()
            if (!trigrams.containsKey(hash)) {
                trigrams[hash] = mutableListOf(i)
            } else {
                trigrams[hash]!!.add(i)
            }
        }

        val all = HashMap<Int, Int>().toMutableMap()

        trigrams.forEach { (_, list) ->
            list.shuffle()
            val cnt = HashMap<Int, Int>().toMutableMap()
            val blockSize = 3
            for (i in 0 until (list.size / blockSize + if (list.size % blockSize > 1) 1 else 0)) {
                val l = mutableListOf<Int>()
                for (j in (i * blockSize) until Math.min((i + 1) * blockSize, list.size)) {
                    l.add(list[j])
                }

                l.sort()
                var gcd = abs(l[1] - l[0])
                for (j in 2 until l.size) {
                    gcd = gcd(gcd, abs(list[j] - list[j - 1]))
                }

                if (!cnt.containsKey(gcd)) {
                    cnt[gcd] = 1
                } else {
                    cnt[gcd]!!.plus(1)
                }
            }

            if (cnt.isNotEmpty()) {
                val best = cnt.toList().sortedBy { it.second }[0]
                if (!all.containsKey(best.first)) {
                    all[best.first] = best.second
                } else {
                    all[best.first] = all[best.first]!! + best.second
                }
            }
        }


        return all.toList().sortedByDescending { it.second }.take(4).map { it.first }.filter { it != 1 }.take(3)
    }
}