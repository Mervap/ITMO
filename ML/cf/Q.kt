package Q

import kotlin.math.ln

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun main() {
  readLongs()
  val n = readInt()
  val xyMap = mutableMapOf<Long, MutableList<Long>>()
  val xs = mutableMapOf<Long, Int>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    xs[x] = xs.getOrDefault(x, 0) + 1
    xyMap.compute(x) { _, list -> list?.apply { add(y) } ?: mutableListOf(y) }
  }

  var ent = 0.0
  for ((x, ys) in xyMap) {
    val probX = xs.getValue(x).toDouble() / n
    val ysX = mutableMapOf<Long, Int>()
    for (y in xyMap.getValue(x)) {
      ysX[y] = ysX.getOrDefault(y, 0) + 1
    }
    val ysCnt = ys.size.toDouble()
    val sumY = ysX.values.fold(0.0) { acc, cnt ->
      val prob = cnt / ysCnt
      acc + prob * ln(prob)
    }
    ent -= probX * sumY
  }
  print(ent)
}
