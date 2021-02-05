package mock.post

import mock.time.UnixTime

data class VKShortPostInfo(val local_id: Long, val owner_id: Long, val from_id: Long, val date: UnixTime)