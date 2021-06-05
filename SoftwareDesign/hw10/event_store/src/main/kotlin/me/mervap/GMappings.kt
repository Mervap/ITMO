package me.mervap

import com.google.protobuf.Timestamp
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import me.mervap.message.*
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


val UserName.gUserName: GUserName
  get() {
    return GUserName.newBuilder()
      .setFirstName(firstName)
      .setLastName(lastName)
      .build()
  }

val LocalDateTime.timestamp: Timestamp
  get() {
    val inst = toInstant()
    return Timestamp.newBuilder()
      .setSeconds(inst.epochSeconds)
      .setNanos(inst.nanosecondsOfSecond)
      .build()
  }

val UpdateSubscriptionEvent.gUpdateSubscription: GUpdateSubscription
  get() {
    return GUpdateSubscription.newBuilder()
      .setId(id)
      .setUserName(userName.gUserName)
      .setPeriod(GPeriod.valueOf(period.name))
      .setPurchaseDate(purchaseDate.timestamp)
      .build()
  }

val SubscriptionInfo.gSubscriptionInfo: GSubscriptionInfo
  get() {
    return GSubscriptionInfo.newBuilder()
      .setId(id)
      .setUserName(userName.gUserName)
      .setExpireDate(expireDate.timestamp)
      .build()
  }

@OptIn(ExperimentalTime::class)
val UpdatableStat.gStat: GStat
  get() {
    return GStat.newBuilder()
      .setId(id)
      .setUserName(userName.gUserName)
      .setPurchaseDate(purchaseDate.timestamp)
      .setLastEnter(lastEnter?.timestamp ?: Timestamp.getDefaultInstance())
      .putAllWeekStat(weekStat.entries.associate { (k, v) -> k.value to v })
      .addAllVisitsAtWeek(visitsAtWeek)
      .addAllVisitsDuration(visitsDuration.map { it.toLongNanoseconds() })
      .build()
  }

val GUserName.userName: UserName
  get() {
    return UserName(firstName, lastName)
  }

val Timestamp.localDateTime: LocalDateTime
  get() {
    return Instant.fromEpochSeconds(seconds, nanos).toLocalDateTime()
  }

val GPeriod.period: Period
  get() {
    return Period.valueOf(name)
  }

val GSubscriptionInfo.subscriptionInfo: SubscriptionInfo
  get() {
    return SubscriptionInfo(id, userName.userName, expireDate.localDateTime)
  }

fun GTurnstileEvent.getTurnstileEvent(isEnter: Boolean): TurnstileEvent {
  return TurnstileEvent(id, isEnter, date.localDateTime)
}

@OptIn(ExperimentalTime::class)
val GStat.updatableStat: UpdatableStat
  get() {
    return UpdatableStat(
      id,
      userName.userName,
      weekStatMap.entries.associate { (k, v) -> DayOfWeek.of(k) to v }.toMutableMap(),
      visitsAtWeekList.toMutableList(),
      visitsDurationList.map { it.toDuration(DurationUnit.NANOSECONDS) }.toMutableList(),
      purchaseDate.localDateTime,
      lastEnter.localDateTime.takeIf { it.toInstant().epochSeconds > 0 }
    )
  }