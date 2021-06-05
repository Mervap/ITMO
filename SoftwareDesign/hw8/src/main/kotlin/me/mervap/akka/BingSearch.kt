package me.mervap.akka

import akka.actor.UntypedAbstractActor
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class BingSearchActor : AbstractSearchActor() {
  override val queryRef = "https://api.bing.microsoft.com/v7.0/search?count=5"

  override fun setupConnection(connection: HttpsURLConnection) {
    connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey)
  }

  override fun parseResponse(response: String): SearchResponse {
    val json = JsonParser.parseString(response).asJsonObject
    val pages = json["webPages"].asJsonObject["value"].asJsonArray
    val links = mutableListOf<String>()
    for (page in pages) {
      links.add(page.asJsonObject["url"].asString)
    }
    return SearchResponse(links, "bing")
  }

  companion object {
    private const val subscriptionKey = "f2d816815f184c45a89953e87b073f89"
  }
}
