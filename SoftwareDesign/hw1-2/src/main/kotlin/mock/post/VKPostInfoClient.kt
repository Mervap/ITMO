package mock.post

import mock.http.HTTPUtil
import mock.time.UnixTime

private const val NEWS_SEARCH_METHOD_NAME = "newsfeed.search"

open class VKPostInfoClient(private val accessToken: String, private val apiVersion: String) {
  /**
   * @throws [IllegalArgumentException] if [hours] less then 1
   */
  @Throws(IllegalArgumentException::class)
  open fun getPostsByHashtag(hashtag: String, hours: Long, endTime: UnixTime): List<VKShortPostInfo> {
    if (hours < 1) {
      throw IllegalArgumentException("Possible to view posts for at least 1 hour")
    }

    val startTime = endTime.addHours(-hours)
    return doApiRequest(NEWS_SEARCH_METHOD_NAME, "q=%23$hashtag", "start_time=$startTime", "end_time=$endTime")
  }

  open fun doApiRequest(methodName: String, vararg parameters: String): List<VKShortPostInfo> {
    val url = createApiUrl(methodName, *parameters)
    return VKPostInfoParser.parseShortPostInfos(HTTPUtil.readText(url))
  }

  private fun createApiUrl(methodName: String, vararg parameters: String): String {
    val joinedParameters = parameters.joinToString("&")
    return "https://api.vk.com/method/$methodName?$joinedParameters&access_token=$accessToken&v=$apiVersion"
  }
}