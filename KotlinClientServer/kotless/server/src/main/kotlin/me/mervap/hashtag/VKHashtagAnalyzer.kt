package me.mervap.hashtag

import khttp.get
import kotlinx.serialization.json.*
import me.mervap.time.UnixTime
import kotlin.math.*

data class Interval(val start: UnixTime, val end: UnixTime)
data class VkIntervalPostStat(val interval: Interval, val cnt: Int)

const val MAX_COUNT_PER_QUERY = 200
const val MAX_QUERIES_CNT = 5 // 200 big queries per day

object VKHashtagAnalyzer {

  fun getHashtagStatisticByLastNHours(hashtag: String, hours: Long): List<VkIntervalPostStat> {
    val curTime = UnixTime.getCurrentTime()
    val stQueryTime = curTime.addHours(-hours)

    val posts = mutableListOf<VKShortPostInfo>()
    var queriesCnt = 0
    var endQueryTime = curTime
    while (queriesCnt < MAX_QUERIES_CNT) {
      val nextPosts = VKPostInfoClient.getPostsByHashtag(hashtag, stQueryTime, endQueryTime)
      posts.addAll(nextPosts)
      if (nextPosts.size < MAX_COUNT_PER_QUERY) {
        break
      }
      endQueryTime = nextPosts.last().date.addSeconds(-1)
      ++queriesCnt
    }
    if (posts.isEmpty()) return emptyList()

    val stat = mutableListOf<VkIntervalPostStat>()
    val groupBy = max(60, (posts.first().date - posts.last().date).seconds / 30) // ~30 intervals
    var stIntervalDate = UnixTime(max(stQueryTime.seconds, posts.last().date.seconds - 1 - groupBy))
    var endIntervalDate = stIntervalDate.addSeconds(groupBy)
    var cnt = 0
    for (post in posts.asReversed()) {
      val secDate = post.date.seconds
      while (secDate > endIntervalDate.seconds) {
        stat.add(VkIntervalPostStat(Interval(stIntervalDate, endIntervalDate), cnt))
        stIntervalDate = endIntervalDate
        endIntervalDate = stIntervalDate.addSeconds(groupBy)
        cnt = 0
      }
      ++cnt
    }
    val endStatTime = min(curTime.seconds, posts.first().date.seconds + 1 + groupBy)
    while (stIntervalDate.seconds < endStatTime) {
      val boundedEnd = UnixTime(min(endIntervalDate.seconds, curTime.seconds))
      stat.add(VkIntervalPostStat(Interval(stIntervalDate, boundedEnd), cnt))
      stIntervalDate = boundedEnd
      endIntervalDate = stIntervalDate.addSeconds(groupBy)
      cnt = 0
    }
    return stat
  }
}

data class VKShortPostInfo(val local_id: Long, val owner_id: Long, val from_id: Long, val date: UnixTime)

object VKPostInfoClient {
  fun getPostsByHashtag(hashtag: String, stTime: UnixTime, endTime: UnixTime): List<VKShortPostInfo> {
    val url = createApiUrl("q=%23$hashtag", "start_time=$stTime", "end_time=$endTime", "count=$MAX_COUNT_PER_QUERY")
    return parseShortPostInfos(get(url).text)
  }

  private fun createApiUrl(vararg parameters: String): String {
    val joinedParameters = parameters.joinToString("&")
    return "https://api.vk.com/method/$NEWS_SEARCH_METHOD_NAME?$joinedParameters&access_token=$SERVICE_ACCESS_TOKEN&v=$API_VERSION"
  }

  private fun parseShortPostInfos(response: String): List<VKShortPostInfo> {
    val jResponse = Json.parseToJsonElement(response)
    val entries = jResponse.jsonObject["response"]?.jsonObject?.get("items")?.jsonArray ?: return emptyList()
    return entries.map {
      val obj = it.jsonObject
      VKShortPostInfo(
        obj["id"]!!.jsonPrimitive.long,
        obj["owner_id"]!!.jsonPrimitive.long,
        obj["from_id"]!!.jsonPrimitive.long,
        UnixTime(obj["date"]!!.jsonPrimitive.long)
      )
    }
  }

  private const val NEWS_SEARCH_METHOD_NAME = "newsfeed.search"
  private const val SERVICE_ACCESS_TOKEN = "196c182d196c182d196c182dd71919b0291196c196c182d468a9ea05dec17f873041cd0"
  private const val API_VERSION = "5.126"
}