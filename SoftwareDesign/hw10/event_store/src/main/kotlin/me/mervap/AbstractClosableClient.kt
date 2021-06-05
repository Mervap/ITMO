package me.mervap

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import io.grpc.kotlin.AbstractCoroutineStub
import java.io.Closeable
import java.util.concurrent.TimeUnit

abstract class AbstractClosableClient<T : AbstractCoroutineStub<*>>(port: Int, stub: Class<T >) : Closeable {
  private val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
  protected val stub: T = stub.getConstructor(Channel::class.java).newInstance(channel) as T

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}