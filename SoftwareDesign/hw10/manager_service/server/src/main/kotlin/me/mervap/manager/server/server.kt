package me.mervap.manager.server

import com.google.protobuf.Empty
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mervap.*
import me.mervap.message.EventStoreServiceGrpcKt.EventStoreServiceCoroutineStub

private object EventStoreClient :
  AbstractClosableClient<EventStoreServiceCoroutineStub>(50051, EventStoreServiceCoroutineStub::class.java) {

  suspend fun buy(updateSubscriptionEvent: UpdateSubscriptionEvent) {
    stub.buySubscription(updateSubscriptionEvent.gUpdateSubscription)
  }

  suspend fun extend(updateSubscriptionEvent: UpdateSubscriptionEvent) {
    stub.extendSubscription(updateSubscriptionEvent.gUpdateSubscription)
  }

  suspend fun getAll(): List<SubscriptionInfo> {
    val infos = stub.getAllSubscriptions(Empty.getDefaultInstance()).infosList
    return infos.map { it.subscriptionInfo }
  }
}

internal fun Application.main() {

  install(CORS) {
    anyHost()
    allowCredentials = true
    allowNonSimpleContentTypes = true
  }

  routing {

    static {
      resources("/")
      defaultResource("index.html", "/")
      resource("buy", "index.html")
      resource("extend", "index.html")
    }

    post("/buy") {
      try {
        EventStoreClient.buy(Json.decodeFromString(call.receiveText()))
        call.respond(HttpStatusCode.OK)
      } catch (e: RuntimeException) {
        call.respond(HttpStatusCode.InternalServerError)
      }
    }

    post("/extend") {
      try {
        EventStoreClient.extend(Json.decodeFromString(call.receiveText()))
        call.respond(HttpStatusCode.OK)
      } catch (e: RuntimeException) {
        call.respond(HttpStatusCode.InternalServerError)
      }
    }

    get("/all_subs") {
      try {
        val subs = EventStoreClient.getAll()
        call.respond(Json.encodeToString(subs))
      } catch (e: RuntimeException) {
        println(e.message)
        call.respond(HttpStatusCode.InternalServerError)
      }
    }
  }
}