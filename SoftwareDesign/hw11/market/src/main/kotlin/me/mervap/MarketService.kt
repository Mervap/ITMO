package me.mervap

import com.google.protobuf.Empty
import me.mervap.market.*

internal class MarketService : MarketServiceGrpcKt.MarketServiceCoroutineImplBase() {
  override suspend fun newCompany(request: GCompanyDesc): GCommandResponse {
    return Storage.addNewCompany(request.kCompanyDesc).gCommandResponse
  }

  override suspend fun newStock(request: GStockDesc): GCommandResponse {
    return Storage.addNewStock(request.kStockDesc).gCommandResponse
  }

  override suspend fun buyStock(request: GOpStokDesc): GCommandResponse {
    return Storage.buyStock(request.opStockDesc).gCommandResponse
  }

  override suspend fun sellStock(request: GOpStokDesc): GCommandResponse {
    return Storage.sellStock(request.opStockDesc).gCommandResponse
  }

  override suspend fun companyList(request: Empty): GCompanyDescList {
    return Storage.getCompanyList().gCompanyDescList
  }

  override suspend fun stocksList(request: Empty): GStockDescList {
    return Storage.getStockList().gStockDescList
  }
}
