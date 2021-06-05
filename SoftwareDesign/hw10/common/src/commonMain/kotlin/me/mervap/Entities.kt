@file:UseSerializers(DateSerializer::class)

package me.mervap

import kotlinx.datetime.*
import kotlinx.datetime.DateTimeUnit.DateBased.DayBased
import kotlinx.datetime.DateTimeUnit.DateBased.MonthBased
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

fun LocalDateTime.toInstant() = toInstant(TimeZone.currentSystemDefault())
fun Instant.toLocalDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())
fun localNow() = Clock.System.now().toLocalDateTime()

@OptIn(ExperimentalSerializationApi::class)
@Serializer(LocalDateTime::class)
private object DateSerializer : KSerializer<LocalDateTime> {
  override val descriptor = InstantSurrogate.serializer().descriptor

  override fun serialize(encoder: Encoder, value: LocalDateTime) {
    val instant = value.toInstant()
    val surrogate = InstantSurrogate(instant.epochSeconds, instant.nanosecondsOfSecond)
    encoder.encodeSerializableValue(InstantSurrogate.serializer(), surrogate)
  }

  override fun deserialize(decoder: Decoder): LocalDateTime {
    val surrogate = decoder.decodeSerializableValue(InstantSurrogate.serializer())
    val instant = Instant.fromEpochSeconds(surrogate.seconds, surrogate.nanos)
    return instant.toLocalDateTime()
  }

  @Serializable
  @SerialName("LocalDateTime")
  private data class InstantSurrogate(val seconds: Long, val nanos: Int)
}

@Serializable
data class UserName(
  val firstName: String,
  val lastName: String
) {
  override fun toString(): String {
    return "$firstName $lastName"
  }
}

enum class Period {
  DAY,
  WEEK,
  MONTH,
  MONTH_3,
  YEAR;

  fun addToDate(date: LocalDateTime): LocalDateTime = when (this) {
    DAY -> date.plus(DayBased(1))
    WEEK -> date.plus(DayBased(7))
    MONTH -> date.plus(MonthBased(1))
    MONTH_3 -> date.plus(MonthBased(3))
    YEAR -> date.plus(MonthBased(12))
  }
}

operator fun LocalDateTime.plus(unit: DateTimeUnit): LocalDateTime =
  TimeZone.currentSystemDefault().let { toInstant(it).plus(unit, it).toLocalDateTime(it) }

operator fun LocalDateTime.minus(unit: DateTimeUnit): LocalDateTime =
  TimeZone.currentSystemDefault().let { toInstant(it).minus(unit, it).toLocalDateTime(it) }

private const val NANOS_PER_ONE = 1_000_000_000

@OptIn(ExperimentalTime::class)
operator fun LocalDateTime.minus(other: LocalDateTime): Duration {
  val inst = toInstant()
  val otherInst = other.toInstant()
  return (
          (inst.epochSeconds * NANOS_PER_ONE + inst.nanosecondsOfSecond) -
          (otherInst.epochSeconds * NANOS_PER_ONE + otherInst.nanosecondsOfSecond)
      ).toDuration(DurationUnit.NANOSECONDS)
}

@Serializable
data class UpdateSubscriptionEvent(
  val id: String,
  val userName: UserName,
  val isExtend: Boolean,
  val period: Period,
  val purchaseDate: LocalDateTime
)

@Serializable
data class SubscriptionInfo(
  val id: String,
  val userName: UserName,
  val expireDate: LocalDateTime
)

@Serializable
data class TurnstileEvent(
  val id: String,
  val isEnter: Boolean,
  val date: LocalDateTime
)

@OptIn(ExperimentalTime::class)
data class UpdatableStat(
  val id: String,
  val userName: UserName,
  val weekStat: MutableMap<DayOfWeek, Int>,
  val visitsAtWeek: MutableList<Int>,
  val visitsDuration: MutableList<Duration>,
  val purchaseDate: LocalDateTime,
  var lastEnter: LocalDateTime?
) {
  fun update(event: TurnstileEvent) {
    if (event.isEnter) {
      lastEnter = event.date
    } else {
      if (lastEnter != null) {
        weekStat[event.date.dayOfWeek] = weekStat[event.date.dayOfWeek]!! + 1
        val week = ((event.date - purchaseDate).inDays.toInt()) / 7
        while (visitsAtWeek.size <= week) visitsAtWeek.add(0)
        visitsAtWeek[week] = visitsAtWeek[week] + 1
        visitsDuration.add(event.date - lastEnter!!)
        lastEnter = null
      }
    }
  }

  companion object {
    fun defaultInstance(id: String, name: UserName, purchaseDate: LocalDateTime): UpdatableStat {
      return UpdatableStat(
        id,
        name,
        DayOfWeek.values().associate { it to 0 }.toMutableMap(),
        mutableListOf(),
        mutableListOf(),
        purchaseDate,
        null
      )
    }
  }
}