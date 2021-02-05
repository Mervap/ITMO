package mock.time

import java.util.*

data class UnixTime(val seconds: Long) {

  operator fun plus(diffSeconds: Long): UnixTime = addSeconds(diffSeconds)

  fun addSeconds(diffSeconds: Long): UnixTime = UnixTime(this.seconds + diffSeconds)

  fun addMinutes(diffMinutes: Long): UnixTime = addSeconds(diffMinutes * 60)

  fun addHours(diffHours: Long): UnixTime = addMinutes(diffHours * 60)

  override fun toString(): String = seconds.toString()

  companion object {
    fun getCurrentTime(): UnixTime {
      return UnixTime(Date().toInstant().epochSecond)
    }
  }
}