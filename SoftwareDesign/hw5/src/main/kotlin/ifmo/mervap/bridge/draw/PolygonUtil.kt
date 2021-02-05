package ifmo.mervap.bridge.draw

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Point(val x: Double, val y: Double)

object PolygonUtil {

  fun makeRegularPolygon(n: Int, centerX: Double, centerY: Double, radius: Double): List<Point> {
    val result = mutableListOf<Point>()
    val mult = 2 * PI / n
    for (i in 0 until n) {
      result.add(Point(centerX + radius * cos(mult * i), centerY + radius * sin(mult * i)))
    }
    return result
  }
}