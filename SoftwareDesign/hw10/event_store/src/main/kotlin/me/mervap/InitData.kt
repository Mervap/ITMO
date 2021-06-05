package me.mervap

import kotlinx.datetime.DateTimeUnit.Companion.DAY
import kotlinx.datetime.DateTimeUnit.Companion.HOUR
import kotlinx.datetime.DateTimeUnit.Companion.MINUTE
import org.litote.kmongo.id.UUIDStringIdGenerator

private fun newId() = UUIDStringIdGenerator.generateNewId<UpdateSubscriptionEvent>().toString()

private val now = localNow()
private val me = UpdateSubscriptionEvent(
  newId(),
  UserName("Valery", "Teplyakov"),
  false,
  Period.YEAR,
  now - DAY.times(10)
)

private val vasya = UpdateSubscriptionEvent(
  newId(),
  UserName("Vasya", "Pupkin"),
  false,
  Period.WEEK,
  now - DAY.times(14)
)

val initSubs = listOf(
  me, vasya, UpdateSubscriptionEvent(
    vasya.id, UserName("", ""), true, Period.DAY, now - DAY.times(7)
  )
)

val initTurnstile = listOf(
  // Me
  TurnstileEvent(me.id, true, now - DAY.times(10) + HOUR),
  TurnstileEvent(me.id, false, now - DAY.times(10) + HOUR.times(2)),
  TurnstileEvent(me.id, true, now - DAY.times(8)),
  TurnstileEvent(me.id, false, now - DAY.times(8) + HOUR.times(2)),
  TurnstileEvent(me.id, true, now - DAY.times(7)),
  TurnstileEvent(me.id, false, now - DAY.times(7) + HOUR.times(3)),
  TurnstileEvent(me.id, true, now - DAY.times(5)),
  TurnstileEvent(me.id, false, now - DAY.times(5) + MINUTE.times(45)),
  TurnstileEvent(me.id, true, now - DAY.times(1)),
  TurnstileEvent(me.id, false, now - DAY.times(1) + MINUTE.times(45)),
  TurnstileEvent(me.id, true, now),
  TurnstileEvent(me.id, false, now + HOUR),

  // Vasya
  TurnstileEvent(vasya.id, true, now - DAY.times(14) + HOUR),
  TurnstileEvent(vasya.id, false, now - DAY.times(14) + HOUR + MINUTE.times(40)),
  TurnstileEvent(vasya.id, true, now - DAY.times(13) + HOUR),
  TurnstileEvent(vasya.id, false, now - DAY.times(13) + HOUR + MINUTE.times(30)),
  TurnstileEvent(vasya.id, true, now - DAY.times(12) + HOUR),
  TurnstileEvent(vasya.id, false, now - DAY.times(12) + HOUR + MINUTE.times(20)),
  TurnstileEvent(vasya.id, true, now - DAY.times(11) + HOUR),
  TurnstileEvent(vasya.id, false, now - DAY.times(11) + HOUR + MINUTE.times(10)),
)