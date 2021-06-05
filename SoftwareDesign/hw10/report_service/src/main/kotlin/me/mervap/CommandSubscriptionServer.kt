package me.mervap

import com.google.protobuf.Empty
import kotlinx.coroutines.runBlocking
import me.mervap.message.GTurnstileEvent

class CommandSubscriptionServer(private val stats: MutableMap<String, UpdatableStat>) : AbstractCommandSubscriptionService() {

  override suspend fun tryEnterCommand(request: GTurnstileEvent): Empty {
    processEvent(request, true)
    return Empty.getDefaultInstance()
  }

  override suspend fun exitCommand(request: GTurnstileEvent): Empty {
    processEvent(request, false)
    return Empty.getDefaultInstance()
  }

  private fun processEvent(request: GTurnstileEvent, isEnter: Boolean) {
    val id = request.id
    stats.computeIfAbsent(id) {
      runBlocking {
        val name = EventStoreClient.findNameById(id)
        val purchaseDate = EventStoreClient.findPurchaseDateById(id)
        UpdatableStat.defaultInstance(id, name, purchaseDate)
      }
    }.update(request.getTurnstileEvent(isEnter))
  }
}