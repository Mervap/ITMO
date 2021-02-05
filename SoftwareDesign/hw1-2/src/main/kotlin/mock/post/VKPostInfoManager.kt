package mock.post

import mock.time.UnixTime

class VKPostInfoManager(private val client: VKPostInfoClient) {

  fun getHashtagStatisticByLastNHours(hashtag: String, hours: Long): List<Int> {
    val curTime = UnixTime.getCurrentTime()
    val posts = client.getPostsByHashtag(hashtag, hours, curTime)
    val stat = mutableListOf<Int>()
    for (i in 1..hours) {
      val l = curTime.addHours(-i).seconds
      val r = curTime.addHours(-i + 1).seconds
      stat.add(posts.count {
        val secDate = it.date.seconds
        secDate in l..r
      })
    }
    return stat
  }
}