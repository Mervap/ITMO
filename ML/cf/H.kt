package H

import java.util.*

fun generateAllVectors(len: Int): List<List<Int>> {
  val res = mutableListOf<List<Int>>()
  fun generateAllVectorsInner(len: Int, curLen: Int, vec: List<Int>) {
    if (len == curLen) {
      res.add(vec)
      return
    }
    generateAllVectorsInner(len, curLen + 1, vec + 0)
    generateAllVectorsInner(len, curLen + 1, vec + 1)
  }

  generateAllVectorsInner(len, 0, mutableListOf())
  return res
}

fun printCore(valuesInd: List<Int>, vec: List<List<Int>>) {
  for (i in valuesInd) {
    var cnt = 0
    for (b in vec[i]) {
      if (b == 1) {
        print("1 ")
        ++cnt
      }
      else print("-1000000000 ")
    }
    println(0.5 - cnt)
  }
}

fun main() = with(Scanner(System.`in`)) {
  val n = nextInt()
  val vec = generateAllVectors(n)
  val q = vec.size
  val oneValuesInd = mutableListOf<Int>()
  val zeroValuesInd = mutableListOf<Int>()
  for (i in 0 until q) {
    val v = nextInt()
    if (v == 0) {
      zeroValuesInd.add(i)
    } else {
      oneValuesInd.add(i)
    }
  }

  if (oneValuesInd.size > 512) {
    println("2\n${zeroValuesInd.size} 1")
    printCore(zeroValuesInd, vec)
    for (i in zeroValuesInd.indices) {
      print("-1 ")
    }
    print("0.5")
  } else {
    if (oneValuesInd.isEmpty()) {
      println("1\n1")
      for (i in 0 until n) {
        print("0 ")
      }
      print("-0.5")
      return
    }

    println("2\n${oneValuesInd.size} 1")
    printCore(oneValuesInd, vec)
    for (i in oneValuesInd.indices) {
      print("1 ")
    }
    print("-0.5")
  }
}