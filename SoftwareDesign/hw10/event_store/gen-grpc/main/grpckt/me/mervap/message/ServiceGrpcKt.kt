package me.mervap.message

import com.google.protobuf.Empty
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import io.grpc.CallOptions
import io.grpc.CallOptions.DEFAULT
import io.grpc.Channel
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import io.grpc.ServerServiceDefinition
import io.grpc.ServerServiceDefinition.builder
import io.grpc.ServiceDescriptor
import io.grpc.Status
import io.grpc.Status.UNIMPLEMENTED
import io.grpc.StatusException
import io.grpc.kotlin.AbstractCoroutineServerImpl
import io.grpc.kotlin.AbstractCoroutineStub
import io.grpc.kotlin.ClientCalls
import io.grpc.kotlin.ClientCalls.unaryRpc
import io.grpc.kotlin.ServerCalls
import io.grpc.kotlin.ServerCalls.unaryServerMethodDefinition
import io.grpc.kotlin.StubFor
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import me.mervap.message.EventStoreServiceGrpc.getServiceDescriptor

/**
 * Holder for Kotlin coroutine-based client and server APIs for me.mervap.EventStoreService.
 */
object EventStoreServiceGrpcKt {
  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = EventStoreServiceGrpc.getServiceDescriptor()

  val buySubscriptionMethod: MethodDescriptor<GUpdateSubscription, CommandResponse>
    @JvmStatic
    get() = EventStoreServiceGrpc.getBuySubscriptionMethod()

  val extendSubscriptionMethod: MethodDescriptor<GUpdateSubscription, CommandResponse>
    @JvmStatic
    get() = EventStoreServiceGrpc.getExtendSubscriptionMethod()

  val tryEnterMethod: MethodDescriptor<GTurnstileEvent, CommandResponse>
    @JvmStatic
    get() = EventStoreServiceGrpc.getTryEnterMethod()

  val exitMethod: MethodDescriptor<GTurnstileEvent, Empty>
    @JvmStatic
    get() = EventStoreServiceGrpc.getExitMethod()

  val getAllSubscriptionsMethod: MethodDescriptor<Empty, GSubscriptionInfoList>
    @JvmStatic
    get() = EventStoreServiceGrpc.getGetAllSubscriptionsMethod()

  val getStatMethod: MethodDescriptor<Empty, GStatList>
    @JvmStatic
    get() = EventStoreServiceGrpc.getGetStatMethod()

  val findNameByIdMethod: MethodDescriptor<StringValue, GUserName>
    @JvmStatic
    get() = EventStoreServiceGrpc.getFindNameByIdMethod()

  val findPurchaseDateByIdMethod: MethodDescriptor<StringValue, Timestamp>
    @JvmStatic
    get() = EventStoreServiceGrpc.getFindPurchaseDateByIdMethod()

  val subscribeOnCommandsMethod: MethodDescriptor<Int32Value, CommandResponse>
    @JvmStatic
    get() = EventStoreServiceGrpc.getSubscribeOnCommandsMethod()

  val unsubscribeFromCommandsMethod: MethodDescriptor<Int32Value, Empty>
    @JvmStatic
    get() = EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod()

  /**
   * A stub for issuing RPCs to a(n) me.mervap.EventStoreService service as suspending coroutines.
   */
  @StubFor(EventStoreServiceGrpc::class)
  class EventStoreServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<EventStoreServiceCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions): EventStoreServiceCoroutineStub =
        EventStoreServiceCoroutineStub(channel, callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun buySubscription(request: GUpdateSubscription): CommandResponse = unaryRpc(
      channel,
      EventStoreServiceGrpc.getBuySubscriptionMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun extendSubscription(request: GUpdateSubscription): CommandResponse = unaryRpc(
      channel,
      EventStoreServiceGrpc.getExtendSubscriptionMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun tryEnter(request: GTurnstileEvent): CommandResponse = unaryRpc(
      channel,
      EventStoreServiceGrpc.getTryEnterMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun exit(request: GTurnstileEvent): Empty = unaryRpc(
      channel,
      EventStoreServiceGrpc.getExitMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun getAllSubscriptions(request: Empty): GSubscriptionInfoList = unaryRpc(
      channel,
      EventStoreServiceGrpc.getGetAllSubscriptionsMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun getStat(request: Empty): GStatList = unaryRpc(
      channel,
      EventStoreServiceGrpc.getGetStatMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun findNameById(request: StringValue): GUserName = unaryRpc(
      channel,
      EventStoreServiceGrpc.getFindNameByIdMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun findPurchaseDateById(request: StringValue): Timestamp = unaryRpc(
      channel,
      EventStoreServiceGrpc.getFindPurchaseDateByIdMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun subscribeOnCommands(request: Int32Value): CommandResponse = unaryRpc(
      channel,
      EventStoreServiceGrpc.getSubscribeOnCommandsMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun unsubscribeFromCommands(request: Int32Value): Empty = unaryRpc(
      channel,
      EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod(),
      request,
      callOptions,
      Metadata()
    )}

  /**
   * Skeletal implementation of the me.mervap.EventStoreService service based on Kotlin coroutines.
   */
  abstract class EventStoreServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.buySubscription.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun buySubscription(request: GUpdateSubscription): CommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.buySubscription is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.extendSubscription.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun extendSubscription(request: GUpdateSubscription): CommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.extendSubscription is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.tryEnter.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun tryEnter(request: GTurnstileEvent): CommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.tryEnter is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.exit.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun exit(request: GTurnstileEvent): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.exit is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.getAllSubscriptions.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun getAllSubscriptions(request: Empty): GSubscriptionInfoList = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.getAllSubscriptions is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.getStat.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun getStat(request: Empty): GStatList = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.getStat is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.findNameById.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun findNameById(request: StringValue): GUserName = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.findNameById is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.findPurchaseDateById.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun findPurchaseDateById(request: StringValue): Timestamp = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.findPurchaseDateById is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.subscribeOnCommands.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun subscribeOnCommands(request: Int32Value): CommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.subscribeOnCommands is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.EventStoreService.unsubscribeFromCommands.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun unsubscribeFromCommands(request: Int32Value): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.EventStoreService.unsubscribeFromCommands is unimplemented"))

    final override fun bindService(): ServerServiceDefinition = builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getBuySubscriptionMethod(),
      implementation = ::buySubscription
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getExtendSubscriptionMethod(),
      implementation = ::extendSubscription
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getTryEnterMethod(),
      implementation = ::tryEnter
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getExitMethod(),
      implementation = ::exit
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getGetAllSubscriptionsMethod(),
      implementation = ::getAllSubscriptions
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getGetStatMethod(),
      implementation = ::getStat
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getFindNameByIdMethod(),
      implementation = ::findNameById
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getFindPurchaseDateByIdMethod(),
      implementation = ::findPurchaseDateById
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getSubscribeOnCommandsMethod(),
      implementation = ::subscribeOnCommands
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = EventStoreServiceGrpc.getUnsubscribeFromCommandsMethod(),
      implementation = ::unsubscribeFromCommands
    )).build()
  }
}

/**
 * Holder for Kotlin coroutine-based client and server APIs for
 * me.mervap.CommandSubscriptionService.
 */
object CommandSubscriptionServiceGrpcKt {
  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = CommandSubscriptionServiceGrpc.getServiceDescriptor()

  val buySubscriptionCommandMethod: MethodDescriptor<GUpdateSubscription, Empty>
    @JvmStatic
    get() = CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod()

  val extendSubscriptionCommandMethod: MethodDescriptor<GUpdateSubscription, Empty>
    @JvmStatic
    get() = CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod()

  val tryEnterCommandMethod: MethodDescriptor<GTurnstileEvent, Empty>
    @JvmStatic
    get() = CommandSubscriptionServiceGrpc.getTryEnterCommandMethod()

  val exitCommandMethod: MethodDescriptor<GTurnstileEvent, Empty>
    @JvmStatic
    get() = CommandSubscriptionServiceGrpc.getExitCommandMethod()

  /**
   * A stub for issuing RPCs to a(n) me.mervap.CommandSubscriptionService service as suspending
   * coroutines.
   */
  @StubFor(CommandSubscriptionServiceGrpc::class)
  class CommandSubscriptionServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<CommandSubscriptionServiceCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions):
        CommandSubscriptionServiceCoroutineStub = CommandSubscriptionServiceCoroutineStub(channel,
        callOptions)

    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun buySubscriptionCommand(request: GUpdateSubscription): Empty = unaryRpc(
      channel,
      CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun extendSubscriptionCommand(request: GUpdateSubscription): Empty = unaryRpc(
      channel,
      CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun tryEnterCommand(request: GTurnstileEvent): Empty = unaryRpc(
      channel,
      CommandSubscriptionServiceGrpc.getTryEnterCommandMethod(),
      request,
      callOptions,
      Metadata()
    )
    /**
     * Executes this RPC and returns the response message, suspending until the RPC completes
     * with [`Status.OK`][Status].  If the RPC completes with another status, a corresponding
     * [StatusException] is thrown.  If this coroutine is cancelled, the RPC is also cancelled
     * with the corresponding exception as a cause.
     *
     * @param request The request message to send to the server.
     *
     * @return The single response from the server.
     */
    suspend fun exitCommand(request: GTurnstileEvent): Empty = unaryRpc(
      channel,
      CommandSubscriptionServiceGrpc.getExitCommandMethod(),
      request,
      callOptions,
      Metadata()
    )}

  /**
   * Skeletal implementation of the me.mervap.CommandSubscriptionService service based on Kotlin
   * coroutines.
   */
  abstract class CommandSubscriptionServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for
     * me.mervap.CommandSubscriptionService.buySubscriptionCommand.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun buySubscriptionCommand(request: GUpdateSubscription): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.CommandSubscriptionService.buySubscriptionCommand is unimplemented"))

    /**
     * Returns the response to an RPC for
     * me.mervap.CommandSubscriptionService.extendSubscriptionCommand.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun extendSubscriptionCommand(request: GUpdateSubscription): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.CommandSubscriptionService.extendSubscriptionCommand is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.CommandSubscriptionService.tryEnterCommand.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun tryEnterCommand(request: GTurnstileEvent): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.CommandSubscriptionService.tryEnterCommand is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.CommandSubscriptionService.exitCommand.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun exitCommand(request: GTurnstileEvent): Empty = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.CommandSubscriptionService.exitCommand is unimplemented"))

    final override fun bindService(): ServerServiceDefinition =
        builder(CommandSubscriptionServiceGrpc.getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = CommandSubscriptionServiceGrpc.getBuySubscriptionCommandMethod(),
      implementation = ::buySubscriptionCommand
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = CommandSubscriptionServiceGrpc.getExtendSubscriptionCommandMethod(),
      implementation = ::extendSubscriptionCommand
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = CommandSubscriptionServiceGrpc.getTryEnterCommandMethod(),
      implementation = ::tryEnterCommand
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = CommandSubscriptionServiceGrpc.getExitCommandMethod(),
      implementation = ::exitCommand
    )).build()
  }
}
