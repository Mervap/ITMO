package P

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun main() {
  readLongs()
  val n = readInt()
  val ys = mutableMapOf<Long, Long>()
  val xs = mutableMapOf<Long, Long>()
  val xyMap = mutableMapOf<Pair<Long, Long>, Long>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    xs[x] = xs.getOrDefault(x, 0) + 1
    ys[y] = ys.getOrDefault(y, 0) + 1
    xyMap[x to y] = xyMap.getOrDefault(x to y, 0) + 1
  }

  var sumExp = 0.0
  var hi = 0.0
  for ((xy, cnt) in xyMap) {
    val (x, y) = xy
    val expected = (xs.getValue(x) * ys.getValue(y)).toDouble() / n
    val diff = cnt - expected
    sumExp += expected
    hi += diff * diff / expected
  }

  println(hi + (n - sumExp))
}