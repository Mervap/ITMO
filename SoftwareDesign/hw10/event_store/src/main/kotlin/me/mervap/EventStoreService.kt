package me.mervap

import com.google.protobuf.Empty
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.mervap.db.DB
import me.mervap.message.*
import org.litote.kmongo.id.UUIDStringIdGenerator
import kotlin.time.ExperimentalTime

internal class EventStoreService(mongoPort: Int = 27017) : EventStoreServiceGrpcKt.EventStoreServiceCoroutineImplBase() {

  private val subscriptionClients: MutableMap<Int, CommandSubscriptionClient> = mutableMapOf()
  private val db = DB(mongoPort)

  override suspend fun buySubscription(request: GUpdateSubscription): CommandResponse {
    println("buySubscription for ${request.id} on ${request.period}")
    val upd = UpdateSubscriptionEvent(
      UUIDStringIdGenerator.generateNewId<UpdateSubscriptionEvent>().toString(),
      request.userName.userName,
      false,
      request.period.period,
      request.purchaseDate.localDateTime,
    )
    return if (db.updateSubscription(upd)) {
      for (client in subscriptionClients.values) client.buySubscriptionCommand(request)
      CommandResponse.newBuilder().setOk(true).setMessage("Id of sub: ${upd.id}").build()
    } else {
      CommandResponse.newBuilder().setOk(false).build()
    }
  }

  override suspend fun extendSubscription(request: GUpdateSubscription): CommandResponse {
    println("extendSubscription for ${request.id} on ${request.period}")
    if (!checkExpiration(request.id).ok) {
      return CommandResponse.newBuilder().setOk(false).setMessage("No active subscription for ${request.id}").build()
    }
    val upd = UpdateSubscriptionEvent(
      request.id,
      request.userName.userName,
      true,
      request.period.period,
      request.purchaseDate.localDateTime,
    )
    return if (db.updateSubscription(upd)) {
      for (client in subscriptionClients.values) client.extendSubscriptionCommand(request)
      CommandResponse.newBuilder().setOk(true).setMessage("Subscription extended").build()
    } else {
      CommandResponse.newBuilder().setOk(false).build()
    }
  }

  override suspend fun tryEnter(request: GTurnstileEvent): CommandResponse {
    println("tryEnter for ${request.id}")
    val checkResp = checkExpiration(request.id)
    if (!checkResp.ok) return checkResp
    return if (db.addTurnstileEvent(request.getTurnstileEvent(true))) {
      for (client in subscriptionClients.values) client.tryEnterCommand(request)
      CommandResponse.newBuilder().setOk(true).build()
    } else {
      CommandResponse.newBuilder().setOk(false).setMessage("Internal error").build()
    }
  }

  private suspend fun checkExpiration(id: String): CommandResponse {
    var lastOkDate: LocalDateTime? = null
    db.findUpdById(id) { lastOkDate = lastOkDate.update(it) }

    val resp = CommandResponse.newBuilder()
    when {
      lastOkDate == null -> {
        resp.ok = false
        resp.message = "Subscription not found"
      }
      lastOkDate!! < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) -> {
        resp.ok = false
        resp.message = "Subscription is expired"
      }
      else -> resp.ok = true
    }
    return resp.build()
  }

  override suspend fun exit(request: GTurnstileEvent): Empty {
    println("exit for ${request.id}")
    db.addTurnstileEvent(request.getTurnstileEvent(false))
    for (client in subscriptionClients.values) client.exitCommand(request)
    return Empty.getDefaultInstance()
  }

  override suspend fun getAllSubscriptions(request: Empty): GSubscriptionInfoList {
    val subs = mutableMapOf<String, SubscriptionInfo>()
    db.findUpdSub {
      val id = it.id
      val old = subs[id]
      subs[id] = SubscriptionInfo(id, old?.userName ?: it.userName, old?.expireDate.update(it))
    }
    val infos = subs.values.map { it.gSubscriptionInfo }
    return GSubscriptionInfoList.newBuilder().addAllInfos(infos).build()
  }

  @ExperimentalTime
  override suspend fun getStat(request: Empty): GStatList {
    println("getStat")
    val stats = mutableMapOf<String, UpdatableStat>()
    db.findTurnstileEvents { event ->
      val id = event.id
      stats.computeIfAbsent(id) {
        runBlocking {
          val name = db.findNameById(id)!!
          val purchaseDate = db.findPurchaseDateById(id)!!
          UpdatableStat.defaultInstance(id, name, purchaseDate)
        }
      }.update(event)
    }

    return GStatList.newBuilder().addAllStats(stats.values.map { it.gStat }).build()
  }

  override suspend fun findNameById(request: StringValue): GUserName {
    return db.findNameById(request.value)!!.gUserName
  }

  override suspend fun findPurchaseDateById(request: StringValue): Timestamp {
    return db.findPurchaseDateById(request.value)!!.timestamp
  }

  override suspend fun subscribeOnCommands(request: Int32Value): CommandResponse {
    val port = request.value
    subscriptionClients[port]?.close()
    subscriptionClients[port] = CommandSubscriptionClient(port)
    return CommandResponse.newBuilder().setOk(true).setMessage("Subscribed on port $port").build()
  }

  override suspend fun unsubscribeFromCommands(request: Int32Value): Empty {
    val port = request.value
    subscriptionClients.remove(port)?.close()
    return Empty.getDefaultInstance()
  }
}

private fun LocalDateTime?.update(upd: UpdateSubscriptionEvent): LocalDateTime = if (upd.isExtend) {
  this!!
  if (upd.purchaseDate < this) {
    upd.period.addToDate(this)
  } else {
    upd.period.addToDate(upd.purchaseDate)
  }
} else {
  upd.period.addToDate(upd.purchaseDate)
}