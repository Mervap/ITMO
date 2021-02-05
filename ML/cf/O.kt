package O

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun calcExpected(n: Int, xs: Map<Long, Int>): Double {
  var expected = 0.0
  for ((value, cnt) in xs) {
    expected += value.toDouble() * cnt / n
  }
  return expected
}

fun calcVariance(n: Int, xs: Map<Long, Int>): Double {
  var variance = 0.0
  val expected = calcExpected(n, xs)
  for ((value, cnt) in xs) {
    val dev = value - expected
    variance += dev * dev * cnt / n
  }
  return variance
}

fun main() {
  readInt()
  val n = readInt()
  val xyMap = mutableMapOf<Long, MutableList<Long>>()
  val ys = mutableMapOf<Long, Int>()
  val xs = mutableMapOf<Long, Int>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    xs[x] = xs.getOrDefault(x, 0) + 1
    ys[y] = ys.getOrDefault(y, 0) + 1
    xyMap.compute(x) { _, list -> list?.apply { add(y) } ?: mutableListOf(y) }
  }

  val yVariance = calcVariance(n, ys)
  val values = mutableListOf<Pair<Double, Double>>()
  for ((x, cnt) in xs) {
    val prob = cnt.toDouble() / n
    val ysX = mutableMapOf<Long, Int>()
    for (y in xyMap.getValue(x)) {
      ysX[y] = ysX.getOrDefault(y, 0) + 1
    }
    val expected = calcExpected(xyMap.getValue(x).size, ysX)
    values.add(expected to prob)
  }
  val expected = values.fold(0.0) { acc, (value, prob) -> acc + prob * value }
  val eVariance = values.fold(0.0) { acc, (value, prob) ->
    val dev = value - expected
    acc + prob * dev * dev
  }
  print(yVariance - eVariance)
}
