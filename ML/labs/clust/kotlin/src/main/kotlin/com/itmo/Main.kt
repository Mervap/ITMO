package com.itmo

import java.io.File
import java.util.*
import kotlin.math.*

typealias FeatureList = List<Double>

enum class Metric {
  MANHATTAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0.0
      for (i in x.indices) {
        res += abs(x[i] - y[i])
      }
      return res
    }
  },
  EUCLIDEAN {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = 0.0
      for (i in x.indices) {
        val diff = x[i] - y[i]
        res += diff * diff
      }
      return sqrt(res)
    }
  },
  CHEBYSHEV {
    override fun compute(x: FeatureList, y: FeatureList): Double {
      super.compute(x, y)
      var res = -1.0
      for (i in x.indices) {
        res = max(res, abs(x[i] - y[i]))
      }
      return res
    }
  };

  open fun compute(x: FeatureList, y: FeatureList): Double {
    if (x.size != y.size) throw RuntimeException("Bad feature list comparison")
    return 0.0
  }
}

data class MergeInfo(val first: Int, val second: Int, val dist: Double)

fun ierhClust(data: List<FeatureList>, metric: Metric): List<MergeInfo> {
  val dists = mutableMapOf<Pair<Int, Int>, Double>()
  val distSet =
    data.mapIndexed { ind1, first ->
      ind1 to data.mapIndexedNotNull { ind2, second ->
        if (ind2 <= ind1) return@mapIndexedNotNull null
        val dist = metric.compute(first, second)
        dists[ind1 to ind2] = dist
        dist to ind2
      }.toSortedSet { a, b ->
        when {
          a.first - b.first < -1e-9 -> -1
          a.first - b.first > 1e-9 -> 1
          a.second < b.second -> -1
          a.second > b.second -> 1
          else -> 0
        }
      }
    }.toMap().toMutableMap()
  val sizes = data.indices.map { it to 1 }.toMap().toMutableMap()

  val res = mutableListOf<MergeInfo>()
  val n = data.size - 1
  for (i in 1..n) {
//    val cnt = data.size - i + 1
//    assert(distSet.keys.size == cnt)
//    assert(sizes.size == cnt)
//    assert(distSet.map { it.value.size }.sum() == cnt * (cnt - 1) / 2)
//    assert(dists.size == cnt * (cnt - 1) / 2)
    var minInd = -1 to -1
    var minD = 1e12
    for ((ind1, set) in distSet) {
      val minSet = set.firstOrNull() ?: continue
      if (minSet.first < minD) {
        minD = minSet.first
        minInd = ind1 to minSet.second
      }
    }

    // RWS := αU*R_US + αV*R_VS + β*R_UV
    val (uInd, vInd) = minInd
    val uSize = sizes.getValue(uInd)
    val vSize = sizes.remove(vInd)!!
    val wSize = uSize + vSize
    sizes[uInd] = wSize
    val rUV = minD
    for ((ind1, set) in distSet) {
      if (ind1 == uInd || ind1 == vInd) continue
      val sSize = sizes.getValue(ind1).toDouble()
      val swSize = sSize + wSize
      val alphaU = (sSize + uSize) / swSize
      val alphaV = (sSize + vSize) / swSize
      val beta = -sSize / swSize
      val rVS =
        if (ind1 < vInd) dists.remove(ind1 to vInd)!!.also { set.remove(it to vInd) }
        else dists.remove(vInd to ind1)!!
      if (ind1 < uInd) {
        val rUS = dists.remove(ind1 to uInd)!!
        set.remove(rUS to uInd)
        val rWS = alphaU * rUS + alphaV * rVS + beta * rUV
        dists[ind1 to uInd] = rWS
        set.add(rWS to uInd)
      }
      else {
        val rUS = dists.remove(uInd to ind1)!!
        distSet.getValue(uInd).remove(rUS to ind1)
        val rWS = alphaU * rUS + alphaV * rVS + beta * rUV
        dists[uInd to ind1] = rWS
        distSet.getValue(uInd).add(rWS to ind1)
      }
    }
    distSet.remove(vInd)
    if (uInd < vInd) {
      distSet.getValue(uInd).remove(dists[uInd to vInd] to vInd)
      dists.remove(uInd to vInd)
    }
    else {
      dists.remove(vInd to uInd)
    }
    res.add(MergeInfo(uInd, vInd, minD))
  }
  return res
}

fun readLn() = readLine()!!
fun readStrings() = readLn().split(" ").filter { it.isNotBlank() }
fun readInts() = readStrings().map { it.toInt() }
fun readDoubles() = readStrings().map { it.toDouble() }

fun main() {// = with(Scanner(File("input.txt").inputStream())) {
  val (objCnt, _) = readInts()
//  val objCnt = nextInt()
//  val f = nextInt()
  val trainingSet: List<FeatureList> = (0 until objCnt).map { readDoubles() }
//  val trainingSet: List<FeatureList> = (0 until objCnt).map {
//    (0 until f).map { nextDouble() }
//  }
//  nextLine()
  val metric = Metric.valueOf(readLn().toUpperCase())
//  val metric = Metric.valueOf(nextLine())
  val mergeInfo = ierhClust(trainingSet, metric)
  for (info in mergeInfo) {
    println("${info.first} ${info.second} ${info.dist}")
  }
}