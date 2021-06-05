package me.mervap.akka

import com.google.gson.JsonParser

class GoogleSearchActor : AbstractSearchActor() {
  override val queryRef = "https://www.googleapis.com/customsearch/v1?key=AIzaSyCZLHX3QMzpOWjY_rlj02n-kFXaaD_KJa8&num=5&cx=eca2037276f9732da"

  override fun parseResponse(response: String): SearchResponse {
    val json = JsonParser.parseString(response).asJsonObject
    val pages = json["items"].asJsonArray
    val links = mutableListOf<String>()
    for (page in pages) {
      links.add(page.asJsonObject["link"].asString)
    }
    return SearchResponse(links, "google")
  }
}