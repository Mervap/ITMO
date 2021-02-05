package me.mervap.time

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*

enum class RoundStage {
  NONE,
  LAST_MINUTE,
  ZERO_MINUTE
}

data class UnixTime(val seconds: Long) {

  operator fun plus(diffSeconds: Long): UnixTime = addSeconds(diffSeconds)

  operator fun plus(other: UnixTime): UnixTime = addSeconds(other.seconds)

  operator fun minus(other: UnixTime): UnixTime = addSeconds(-other.seconds)

  fun addSeconds(diffSeconds: Long): UnixTime = UnixTime(this.seconds + diffSeconds)

  fun addMinutes(diffMinutes: Long): UnixTime = addSeconds(diffMinutes * 60)

  fun addHours(diffHours: Long): UnixTime = addMinutes(diffHours * 60)

  fun dateTimeString(roundStage: RoundStage): String {
    val base = DateTime(seconds * 1000).withZone(DateTimeZone.forOffsetHours(3))
    return when (roundStage) {
      RoundStage.ZERO_MINUTE -> base.toString("yyyy-MM-d HH:00:00")
      RoundStage.LAST_MINUTE -> {
        val res = base.toString("yyyy-MM-d HH:mm")
        val minute = res.last().toString().toInt()
        val roundMinute = if (minute in 3..7) 5 else 0
        res.dropLast(1) + "$roundMinute:00"
      }
      RoundStage.NONE -> base.toString("yyyy-MM-d HH:mm:00")
    }
  }

  override fun toString(): String = seconds.toString()

  companion object {
    fun getCurrentTime(): UnixTime {
      return UnixTime(Date().toInstant().epochSecond)
    }
  }
}