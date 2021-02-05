package com.itmo

data class FMeasure(val micro: Double, val macro: Double)

private fun doCalcF(precision: Double, recall: Double): Double {
  return if (precision + recall > 0) 2.0 * precision * recall / (precision + recall)
  else 0.0
}

fun calcFMeasure(confusionMatrix: Array<IntArray>): FMeasure {
  val size = confusionMatrix.size
  val rowSum = ArrayList<Int>(size)
  val columnSum = ArrayList<Int>(size)
  val ok = ArrayList<Int>(size)
  for (i in 0 until size) {
    rowSum.add(0)
    columnSum.add(0)
    ok.add(0)
  }
  var totalSum = 0
  for (i in confusionMatrix.indices) {
    for (j in confusionMatrix.indices) {
      val elem = confusionMatrix[i][j]
      rowSum[i] += elem
      columnSum[j] += elem
      if (i == j) {
        ok[i] = elem
      }
      totalSum += elem
    }
  }

  var fMacro = 0.0
  var precision = 0.0
  var recall = 0.0
  for (i in 0 until size) {
    var localPrecision = 0.0
    if (rowSum[i] > 0) {
      localPrecision = ok[i].toDouble() / rowSum[i]
    }
    var localRecall = 0.0
    if (columnSum[i] > 0) {
      localRecall = ok[i].toDouble() / columnSum[i]
    }
    precision += localPrecision * rowSum[i]
    recall += localRecall * rowSum[i]
    fMacro += doCalcF(localPrecision, localRecall) * rowSum[i]
  }

  precision /= totalSum
  recall /= totalSum
  return FMeasure(doCalcF(precision, recall), fMacro / totalSum)
}