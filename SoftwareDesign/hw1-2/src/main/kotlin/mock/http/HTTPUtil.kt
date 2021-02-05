package mock.http

import java.io.IOException
import java.io.UncheckedIOException
import java.net.MalformedURLException
import java.net.URL

object HTTPUtil {
  fun readText(sourceUrl: String): String {
    return try {
      toUrl(sourceUrl).readText()
    } catch (e: IOException) {
      throw UncheckedIOException(e)
    }
  }

  private fun toUrl(url: String): URL {
    return try {
      URL(url)
    } catch (e: MalformedURLException) {
      throw RuntimeException("Malformed url: $url")
    }
  }
}