import java.lang.StringBuilder

class Encoder(alf: List<String>) {

    private val permutations: List<Map<Char, Char>> = alf.map { it.mapIndexed { ind, c -> 'a'.plus(ind) to c }.associateBy({ it.first }, { it.second }) }

    fun encode(text: String):String  {
        val res = StringBuilder()
        for (i in text.indices) {
            res.append(permutations[i % permutations.size][text[i].toLowerCase()])
        }

        return res.toString()
    }
}