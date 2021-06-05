package me.mervap.akka

import akka.actor.UntypedAbstractActor
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

data class SearchResponse(val links: List<String>, val engineName: String)

abstract class AbstractSearchActor : UntypedAbstractActor() {

  protected abstract val queryRef: String
  protected abstract fun parseResponse(response: String): SearchResponse
  protected open fun setupConnection(connection: HttpsURLConnection) {}

  override fun onReceive(message: Any?) {
    if (message is String) {
      val url = URL("$queryRef&q=" + URLEncoder.encode(message, "UTF-8"))
      val connection = url.openConnection() as HttpsURLConnection
      setupConnection(connection)
      val response = String(connection.inputStream.readAllBytes(), Charset.defaultCharset())
      val parsed = parseResponse(response)
      context.parent.tell(parsed, self)
    }
  }
}