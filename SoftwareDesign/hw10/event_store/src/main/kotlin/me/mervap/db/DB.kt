package me.mervap.db

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import me.mervap.TurnstileEvent
import me.mervap.UpdateSubscriptionEvent
import me.mervap.UserName
import org.bson.conversions.Bson
import org.litote.kmongo.EMPTY_BSON
import org.litote.kmongo.and
import org.litote.kmongo.ascending
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

internal class DB(port: Int = 27017) {
  private val client = KMongo.createClient("mongodb://localhost:$port").coroutine
  private val database = client.getDatabase("eventSource")
  private val updateSubscriptionCol = database.getCollection<UpdateSubscriptionEvent>()
  private val turnstileEventCol = database.getCollection<TurnstileEvent>()

  init {
    runBlocking {
      updateSubscriptionCol.ensureIndex(UpdateSubscriptionEvent::id)
      updateSubscriptionCol.ensureIndex(UpdateSubscriptionEvent::purchaseDate)

      turnstileEventCol.ensureIndex(TurnstileEvent::id)
      turnstileEventCol.ensureIndex(TurnstileEvent::date)
    }
  }

  suspend fun updateSubscription(upd: UpdateSubscriptionEvent): Boolean {
    return updateSubscriptionCol.insertOne(upd).wasAcknowledged()
  }

  suspend fun findNameById(id: String): UserName? {
    return updateSubscriptionCol.find(and(UpdateSubscriptionEvent::id eq id, UpdateSubscriptionEvent::isExtend eq false))
      .first()?.userName
  }

  suspend fun findPurchaseDateById(id: String): LocalDateTime? {
    return updateSubscriptionCol.find(and(UpdateSubscriptionEvent::id eq id, UpdateSubscriptionEvent::isExtend eq false))
      .first()?.purchaseDate
  }

  suspend fun findUpdById(id: String, action: suspend (UpdateSubscriptionEvent) -> Unit) {
    findUpdSub(UpdateSubscriptionEvent::id eq id, action)
  }

  suspend fun findUpdSub(filter: Bson = EMPTY_BSON, action: suspend (UpdateSubscriptionEvent) -> Unit) {
    updateSubscriptionCol.find(filter).sort(ascending(UpdateSubscriptionEvent::purchaseDate)).consumeEach(action)
  }

  suspend fun addTurnstileEvent(event: TurnstileEvent): Boolean {
    return turnstileEventCol.insertOne(event).wasAcknowledged()
  }

  suspend fun findTurnstileEvents(filter: Bson = EMPTY_BSON, action: suspend (TurnstileEvent) -> Unit) {
    turnstileEventCol.find(filter).sort(ascending(TurnstileEvent::date)).consumeEach(action)
  }
}