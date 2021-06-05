package me.mervap.market

import com.google.protobuf.Empty
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
import me.mervap.market.MarketServiceGrpc.getServiceDescriptor

/**
 * Holder for Kotlin coroutine-based client and server APIs for me.mervap.MarketService.
 */
object MarketServiceGrpcKt {
  @JvmStatic
  val serviceDescriptor: ServiceDescriptor
    get() = MarketServiceGrpc.getServiceDescriptor()

  val newCompanyMethod: MethodDescriptor<GCompanyDesc, GCommandResponse>
    @JvmStatic
    get() = MarketServiceGrpc.getNewCompanyMethod()

  val newStockMethod: MethodDescriptor<GStockDesc, GCommandResponse>
    @JvmStatic
    get() = MarketServiceGrpc.getNewStockMethod()

  val buyStockMethod: MethodDescriptor<GOpStokDesc, GCommandResponse>
    @JvmStatic
    get() = MarketServiceGrpc.getBuyStockMethod()

  val sellStockMethod: MethodDescriptor<GOpStokDesc, GCommandResponse>
    @JvmStatic
    get() = MarketServiceGrpc.getSellStockMethod()

  val companyListMethod: MethodDescriptor<Empty, GCompanyDescList>
    @JvmStatic
    get() = MarketServiceGrpc.getCompanyListMethod()

  val stocksListMethod: MethodDescriptor<Empty, GStockDescList>
    @JvmStatic
    get() = MarketServiceGrpc.getStocksListMethod()

  /**
   * A stub for issuing RPCs to a(n) me.mervap.MarketService service as suspending coroutines.
   */
  @StubFor(MarketServiceGrpc::class)
  class MarketServiceCoroutineStub @JvmOverloads constructor(
    channel: Channel,
    callOptions: CallOptions = DEFAULT
  ) : AbstractCoroutineStub<MarketServiceCoroutineStub>(channel, callOptions) {
    override fun build(channel: Channel, callOptions: CallOptions): MarketServiceCoroutineStub =
        MarketServiceCoroutineStub(channel, callOptions)

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
    suspend fun newCompany(request: GCompanyDesc): GCommandResponse = unaryRpc(
      channel,
      MarketServiceGrpc.getNewCompanyMethod(),
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
    suspend fun newStock(request: GStockDesc): GCommandResponse = unaryRpc(
      channel,
      MarketServiceGrpc.getNewStockMethod(),
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
    suspend fun buyStock(request: GOpStokDesc): GCommandResponse = unaryRpc(
      channel,
      MarketServiceGrpc.getBuyStockMethod(),
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
    suspend fun sellStock(request: GOpStokDesc): GCommandResponse = unaryRpc(
      channel,
      MarketServiceGrpc.getSellStockMethod(),
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
    suspend fun companyList(request: Empty): GCompanyDescList = unaryRpc(
      channel,
      MarketServiceGrpc.getCompanyListMethod(),
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
    suspend fun stocksList(request: Empty): GStockDescList = unaryRpc(
      channel,
      MarketServiceGrpc.getStocksListMethod(),
      request,
      callOptions,
      Metadata()
    )}

  /**
   * Skeletal implementation of the me.mervap.MarketService service based on Kotlin coroutines.
   */
  abstract class MarketServiceCoroutineImplBase(
    coroutineContext: CoroutineContext = EmptyCoroutineContext
  ) : AbstractCoroutineServerImpl(coroutineContext) {
    /**
     * Returns the response to an RPC for me.mervap.MarketService.newCompany.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun newCompany(request: GCompanyDesc): GCommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.newCompany is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.MarketService.newStock.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun newStock(request: GStockDesc): GCommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.newStock is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.MarketService.buyStock.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun buyStock(request: GOpStokDesc): GCommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.buyStock is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.MarketService.sellStock.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun sellStock(request: GOpStokDesc): GCommandResponse = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.sellStock is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.MarketService.companyList.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun companyList(request: Empty): GCompanyDescList = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.companyList is unimplemented"))

    /**
     * Returns the response to an RPC for me.mervap.MarketService.stocksList.
     *
     * If this method fails with a [StatusException], the RPC will fail with the corresponding
     * [Status].  If this method fails with a [java.util.concurrent.CancellationException], the RPC
     * will fail
     * with status `Status.CANCELLED`.  If this method fails for any other reason, the RPC will
     * fail with `Status.UNKNOWN` with the exception as a cause.
     *
     * @param request The request from the client.
     */
    open suspend fun stocksList(request: Empty): GStockDescList = throw
        StatusException(UNIMPLEMENTED.withDescription("Method me.mervap.MarketService.stocksList is unimplemented"))

    final override fun bindService(): ServerServiceDefinition = builder(getServiceDescriptor())
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getNewCompanyMethod(),
      implementation = ::newCompany
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getNewStockMethod(),
      implementation = ::newStock
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getBuyStockMethod(),
      implementation = ::buyStock
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getSellStockMethod(),
      implementation = ::sellStock
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getCompanyListMethod(),
      implementation = ::companyList
    ))
      .addMethod(unaryServerMethodDefinition(
      context = this.context,
      descriptor = MarketServiceGrpc.getStocksListMethod(),
      implementation = ::stocksList
    )).build()
  }
}
