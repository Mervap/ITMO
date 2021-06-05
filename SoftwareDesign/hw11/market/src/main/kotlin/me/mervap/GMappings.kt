package me.mervap

import me.mervap.market.*

val CommandResponse.gCommandResponse: GCommandResponse
  get() {
    return GCommandResponse.newBuilder().setOk(ok).setMessage(message).build()
  }

val GCommandResponse.kCommandResponse: CommandResponse
  get() {
    return CommandResponse(ok, message)
  }

val CompanyDesc.gCompanyDesc: GCompanyDesc
  get() {
    return GCompanyDesc.newBuilder().setId(id).setName(name).setCountry(country).build()
  }

val GCompanyDesc.kCompanyDesc: CompanyDesc
  get() {
    return CompanyDesc(id, name, country)
  }

val List<CompanyDesc>.gCompanyDescList: GCompanyDescList
  get() {
    return GCompanyDescList.newBuilder().addAllCompanies(map { it.gCompanyDesc }).build()
  }

val GCompanyDescList.kCompanyDescList: List<CompanyDesc>
  get() {
    return companiesList.map { it.kCompanyDesc }
  }

val StockDesc.gStockDesc: GStockDesc
  get() {
    return GStockDesc.newBuilder()
      .setId(id)
      .setCompanyId(companyId)
      .setName(name)
      .setPrice(price)
      .setTotalCount(totalCount).build()
  }

val GStockDesc.kStockDesc: StockDesc
  get() {
    return StockDesc(id, companyId, name, price, totalCount)
  }

val List<StockDesc>.gStockDescList: GStockDescList
  get() {
    return GStockDescList.newBuilder().addAllStocks(map { it.gStockDesc }).build()
  }

val GStockDescList.kStockDescList: List<StockDesc>
  get() {
    return stocksList.map { it.kStockDesc }
  }

val OpStockDesc.gOpStockDesc: GOpStokDesc
  get() {
    return GOpStokDesc.newBuilder().setId(id).setCount(count).setBudget(budget).build()
  }

val GOpStokDesc.opStockDesc: OpStockDesc
  get() {
    return OpStockDesc(id, count, budget)
  }