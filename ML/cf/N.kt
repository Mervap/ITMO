package N

private fun readLn() = readLine()!!.trim()
private fun readInt() = readLn().toInt()
private fun readStrings() = readLn().split(" ")
private fun readLongs() = readStrings().map { it.toLong() }

fun main() {
  readInt()
  val n = readInt()
  val xs = mutableListOf<Long>()
  val ys = mutableListOf<Long>()
  for (i in 1..n) {
    val (x, y) = readLongs()
    xs.add(x)
    ys.add(y)
  }
  val minX = xs.min()!!
  val xsScaled = xs.mapIndexed { ind, x -> x - minX to ind }.sortedBy { it.first }
  val classSum = mutableMapOf<Long, Long>()
  val classCnt = mutableMapOf<Long, Long>()
  var allSum = 0L
  var allCnt = 0L
  var innerSum = 0L
  var outerSum = 0L
  for (i in 0 until n) {
    val (x, ind) = xsScaled[i]
    val y = ys[ind]
    classSum.putIfAbsent(y, 0L)
    val yClassSum = classSum.getValue(y)
    classCnt.putIfAbsent(y, 0L)
    val yClassCnt = classCnt.getValue(y)
    innerSum += x * yClassCnt - yClassSum
    outerSum += x * (allCnt - yClassCnt) - (allSum - yClassSum)
    ++allCnt
    classCnt[y] = yClassCnt + 1
    allSum += x
    classSum[y] = yClassSum + x
  }

  println(innerSum * 2)
  println(outerSum * 2)
}