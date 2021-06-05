package me.mervap

import io.grpc.Server
import io.grpc.ServerBuilder
import me.mervap.db.DB

suspend fun main() {
  val port = 50051
  val server = ServerBuilder.forPort(port).addService(EventStoreService()).build().start()
  for (sub in initSubs) DB().updateSubscription(sub)
  for (turn in initTurnstile) DB().addTurnstileEvent(turn)
  server.awaitTermination()
}
