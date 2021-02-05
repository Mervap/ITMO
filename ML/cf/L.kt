package L

import kotlin.math.*

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun main() {
  val n = readInt()
  var xSum = 0L
  var ySum = 0L
  val objs = mutableListOf<Pair<Long, Long>>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    objs.add(x to y)
    xSum += x
    ySum += y
  }
  val xMean = xSum.toDouble() / n
  val yMean = ySum.toDouble() / n
  var xDiffSum = 0.0
  var yDiffSum = 0.0
  var xyDiffProdSum = 0.0
  for ((x, y) in objs) {
    val xDiff = x - xMean
    val yDiff = y - yMean
    xyDiffProdSum += xDiff * yDiff
    xDiffSum += xDiff * xDiff
    yDiffSum += yDiff * yDiff
  }
  if (abs(xDiffSum) < 1e-9 || abs(yDiffSum) < 1e-9) {
    print(0)
  }
  else {
    print(xyDiffProdSum / sqrt(xDiffSum * yDiffSum))
  }
}