import com.google.protobuf.Empty
import io.grpc.ManagedChannelBuilder
import me.mervap.*
import me.mervap.market.MarketServiceGrpcKt
import java.io.Closeable
import java.util.concurrent.TimeUnit

data class User(val id: Int, val name: String, var budget: Double)

object MarketClient : Closeable {
  private val channel = ManagedChannelBuilder.forAddress("localhost", 50055).usePlaintext().build()
  private val stub = MarketServiceGrpcKt.MarketServiceCoroutineStub(channel)

  private val users: MutableMap<Int, User> = mutableMapOf()

  suspend fun newCompany(request: CompanyDesc): CommandResponse {
    return stub.newCompany(request.gCompanyDesc).kCommandResponse
  }

  suspend fun newStock(request: StockDesc): CommandResponse {
    return stub.newStock(request.gStockDesc).kCommandResponse
  }

  suspend fun buyStock(request: OpStockDesc): CommandResponse {
    return stub.buyStock(request.gOpStockDesc).kCommandResponse
  }

  suspend fun sellStock(request: OpStockDesc): CommandResponse {
    return stub.buyStock(request.gOpStockDesc).kCommandResponse
  }

  suspend fun companyList(): List<CompanyDesc> {
    return stub.companyList(Empty.getDefaultInstance()).kCompanyDescList
  }

  suspend fun stocksList(): List<StockDesc> {
    return stub.stocksList(Empty.getDefaultInstance()).kStockDescList
  }

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }
}