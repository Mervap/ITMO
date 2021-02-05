package M

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun calcRang(arr: List<Long>): Map<Long, Long> {
  var rang = 0L
  val resRang = mutableMapOf<Long, Long>()
  for (el in arr.sorted()) {
    ++rang
    resRang[el] = rang
  }
  return resRang
}

fun main() {
  val n = readInt()
  val xs = mutableListOf<Long>()
  val ys = mutableListOf<Long>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    xs.add(x)
    ys.add(y)
  }
  val xsRang = calcRang(xs)
  val ysRang = calcRang(ys)

  var diffSqrSum = 0L
  for (i in 0 until n) {
    val diff = xsRang.getValue(xs[i]) - ysRang.getValue(ys[i])
    diffSqrSum += diff * diff
  }
  print(1.0 - (6 * diffSqrSum).toDouble() / (n * (n.toLong() * n - 1)))
}