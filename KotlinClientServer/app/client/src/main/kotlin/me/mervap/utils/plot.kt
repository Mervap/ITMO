package me.mervap.utils

import kotlin.js.Date

fun calcBarWidth(dates: List<String>): Double? {
  val diff = dates.map { Date(it).getTime() }.zipWithNext { a, b -> (b - a) * 0.8 }.sorted()
  return when {
    diff.isEmpty() -> null
    diff.size < 3 -> diff.last()
    else -> diff[2]
  }
}