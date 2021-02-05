package mock.post

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import mock.time.UnixTime

object VKPostInfoParser {
  fun parseShortPostInfos(response: String): List<VKShortPostInfo> {
    val jResponse = JsonParser.parseString(response) as JsonObject
    val entries = (jResponse["response"] as JsonObject)["items"] as JsonArray
    return entries.map {
      val obj = it as JsonObject
      VKShortPostInfo(obj["id"].asLong, obj["owner_id"].asLong, obj["from_id"].asLong, UnixTime(obj["date"].asLong))
    }
  }
}