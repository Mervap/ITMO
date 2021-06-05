package me.mervap

import com.google.protobuf.Empty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.mervap.message.CommandSubscriptionServiceGrpcKt
import me.mervap.message.CommandSubscriptionServiceGrpcKt.CommandSubscriptionServiceCoroutineStub
import me.mervap.message.GTurnstileEvent
import me.mervap.message.GUpdateSubscription

abstract class AbstractCommandSubscriptionService :
  CommandSubscriptionServiceGrpcKt.CommandSubscriptionServiceCoroutineImplBase() {
  override suspend fun buySubscriptionCommand(request: GUpdateSubscription): Empty = Empty.getDefaultInstance()
  override suspend fun extendSubscriptionCommand(request: GUpdateSubscription): Empty = Empty.getDefaultInstance()
  override suspend fun tryEnterCommand(request: GTurnstileEvent): Empty = Empty.getDefaultInstance()
  override suspend fun exitCommand(request: GTurnstileEvent): Empty = Empty.getDefaultInstance()
}

internal class CommandSubscriptionClient(port: Int) :
  AbstractClosableClient<CommandSubscriptionServiceCoroutineStub>(
    port,
    CommandSubscriptionServiceCoroutineStub::class.java
  ) {

  fun buySubscriptionCommand(request: GUpdateSubscription) {
    GlobalScope.launch { stub.buySubscriptionCommand(request) }
  }

  fun extendSubscriptionCommand(request: GUpdateSubscription) {
    GlobalScope.launch { stub.extendSubscriptionCommand(request) }
  }

  fun tryEnterCommand(request: GTurnstileEvent) {
    GlobalScope.launch { stub.tryEnterCommand(request) }
  }

  fun exitCommand(request: GTurnstileEvent) {
    GlobalScope.launch { stub.exitCommand(request) }
  }
}