package me.mervap

import io.grpc.ServerBuilder

fun main() {
  val server = ServerBuilder.forPort(50055).addService(MarketService()).build().start()
  server.awaitTermination()
}
