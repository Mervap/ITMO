package me.mervap

import kotlin.math.pow
import kotlin.random.Random

data class CompanyDesc(val id: Int, val name: String, val country: String)
data class StockDesc(val id: Int, val companyId: Int, val name: String, val price: Double, val totalCount: Int)
data class CommandResponse(val ok: Boolean, val message: String = "")
data class OpStockDesc(val id: Int, val count: Int, val budget: Double = 0.0)

object Storage {

  private val companies = mutableMapOf<Int, CompanyDesc>()
  private val stocks = mutableMapOf<Int, StockDesc>()

  fun addNewCompany(company: CompanyDesc): CommandResponse {
    randPriceShift()
    val id = company.id
    if (companies.containsKey(id)) {
      return CommandResponse(false, "id '$id' already used")
    }
    companies[id] = company
    return CommandResponse(true)
  }

  fun addNewStock(stock: StockDesc): CommandResponse {
    randPriceShift()
    val id = stock.id
    if (stocks.containsKey(id)) {
      return CommandResponse(false, "id '$id' already used")
    }
    if (!companies.containsKey(stock.companyId)) {
      return CommandResponse(false, "Company '$id' doesn't exist")
    }
    stocks[id] = stock
    return CommandResponse(true)
  }

  fun buyStock(stock: OpStockDesc): CommandResponse {
    randPriceShift()
    val id = stock.id
    val stockInfo = stocks[id] ?: return CommandResponse(false, "id '$id' not found")
    val buyCount = stock.count
    val totalCount = stockInfo.totalCount
    if (buyCount > totalCount) return CommandResponse(
      false,
      "Cant buy $buyCount stocks. Only $totalCount available"
    )
    if (stockInfo.price * buyCount > stock.budget) return CommandResponse(false, "Not enough budget")
    stocks[id] = StockDesc(id, stockInfo.companyId, stockInfo.name, stockInfo.price, totalCount - buyCount)
    return CommandResponse(true)
  }

  fun sellStock(stock: OpStockDesc): CommandResponse {
    randPriceShift()
    val id = stock.id
    val stockInfo = stocks[id] ?: return CommandResponse(false, "id '$id' not found")
    stocks[id] = StockDesc(id, stockInfo.companyId, stockInfo.name, stockInfo.price, stockInfo.totalCount + stock.count)
    return CommandResponse(true)
  }

  fun getCompanyList(): List<CompanyDesc> {
    randPriceShift()
    return companies.values.toList()
  }

  fun getStockList(): List<StockDesc> {
    randPriceShift()
    return stocks.values.toList()
  }

  private fun randPriceShift() {
    val stocks = stocks.values
    for (stock in stocks) {
      val rand = Random(42).nextDouble()
      val newPrice = stock.price + 100 * rand.pow(5)
      this.stocks[stock.id] = StockDesc(stock.id, stock.companyId, stock.name, newPrice, stock.totalCount)
    }
  }
}