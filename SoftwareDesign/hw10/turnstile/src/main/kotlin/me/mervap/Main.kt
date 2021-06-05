package me.mervap

import me.mervap.message.CommandResponse
import me.mervap.message.EventStoreServiceGrpcKt.EventStoreServiceCoroutineStub
import me.mervap.message.GTurnstileEvent

private object TurnstileClient :
  AbstractClosableClient<EventStoreServiceCoroutineStub>(50051, EventStoreServiceCoroutineStub::class.java) {
  suspend fun tryEnter(id: String): CommandResponse {
    return stub.tryEnter(buildEvent(id))
  }

  suspend fun exit(id: String) {
    stub.exit(buildEvent(id))
  }

  private fun buildEvent(id: String): GTurnstileEvent {
    return GTurnstileEvent.newBuilder().setId(id).setDate(localNow().timestamp).build()
  }
}

suspend fun main() {
  while (true) {
    val (command, id) = readLine()!!.split(" ")
    when (command) {
      "enter" -> {
        val resp = TurnstileClient.tryEnter(id)
        if (resp.ok) {
          println("Welcome!")
        } else {
          println("Access denied! ${resp.message}")
        }
      }
      "exit" -> {
        TurnstileClient.exit(id)
        println("Goodbye!")
      }
      else -> System.err.println("Bad command '$command'")
    }
  }
}