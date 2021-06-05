import me.mervap.*

private fun printResp(resp: CommandResponse) {
  if (resp.ok) {
    println("Ok!")
  }
  else {
    println("Err: ${resp.message}")
  }
}


suspend fun main() {
  while (true) {
    val line = readLine()!!.trim()
    when {
      line.startsWith("addc") -> {
        if (line.split(" ").size < 4) {
          println("Wrong count of args")
          continue
        }
        val (_, id, name, country) = line.split(" ")
        val resp = MarketClient.newCompany(CompanyDesc(id.toInt(), name, country))
        printResp(resp)
      }
      line.startsWith("adds") -> {
        if (line.split(" ").size < 6) {
          println("Wrong count of args")
          continue
        }
        val (_, id, companyId, name, price, totalCount) = line.split(" ")
        val resp = MarketClient.newStock(StockDesc(id.toInt(), companyId.toInt(), name, price.toDouble(), totalCount.toInt()))
        printResp(resp)
      }
      line.startsWith("addu") -> {
        if (line.split(" ").size < 4) {
          println("Wrong count of args")
          continue
        }
        val (_, _id, name, budget) = line.split(" ")
        val id = _id.toInt()
//        if (users.containsKey(id)) {
//          println("Err: id 'id' already used")
//        }
//        users[id] = User(id, name, budget.toDouble())
      }
      line == "listc" -> {
        val companies = MarketClient.companyList()
        val template = "| %3s | %15s | %15s |"
        println(template.format("id", "Name", "Country"))
        for (company in companies) {
          println(template.format(company.id, company.name, company.country))
        }
      }
      line == "lists" -> {
        val stocks = MarketClient.stocksList()
        val template = "| %3s | %3s | %15s | %7.2f | %11s |"
        println(template.replace("%7.2f", "%7s").format("id", "Cid", "Name", "Price", "Total count"))
        for (stock in stocks) {
          println(template.format(stock.id, stock.companyId, stock.name, stock.price, stock.totalCount))
        }
      }
      line == "listu" -> {
        val template = "| %3s | %15s | %7.2f |"
        println(template.replace("%7.2f", "%7s").format("id", "Name", "Budget"))
//        for (user in users.values) {
//          println(template.format(user.id, user.name, user.budget))
//        }
      }
      else -> println("Bad command: $line")
    }
  }
}

private operator fun <E> List<E>.component6(): E = get(5)
