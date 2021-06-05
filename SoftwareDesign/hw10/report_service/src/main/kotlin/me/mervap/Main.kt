package me.mervap

import com.google.protobuf.Empty
import com.google.protobuf.Int32Value
import com.google.protobuf.StringValue
import io.grpc.ServerBuilder
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import me.mervap.message.EventStoreServiceGrpcKt.EventStoreServiceCoroutineStub
import kotlin.time.ExperimentalTime

private const val subPort = 50052

internal object EventStoreClient :
  AbstractClosableClient<EventStoreServiceCoroutineStub>(50051, EventStoreServiceCoroutineStub::class.java) {

  suspend fun subscribe() {
    stub.subscribeOnCommands(Int32Value.of(subPort))
  }

  suspend fun findNameById(id: String): UserName {
    return stub.findNameById(StringValue.of(id)).userName
  }

  suspend fun findPurchaseDateById(id: String): LocalDateTime {
    return stub.findPurchaseDateById(StringValue.of(id)).localDateTime
  }

  suspend fun getStats(): List<UpdatableStat> {
    return stub.getStat(Empty.getDefaultInstance()).statsList.map { it.updatableStat }
  }

  override fun close() {
    runBlocking { stub.unsubscribeFromCommands(Int32Value.of(subPort)) }
    super.close()
  }
}


private const val statTemplate = "| %36s | %20s | %3s | %3s | %3s | %3s | %3s | %3s | %3s | %12s | %8s |"

@OptIn(ExperimentalTime::class)
suspend fun main() {
  EventStoreClient.subscribe()
  val stats = EventStoreClient.getStats().associateBy { it.id }.toMutableMap()
  ServerBuilder.forPort(subPort).addService(CommandSubscriptionServer(stats)).build().start()
  while (true) {
    when (val command = readLine()) {
      "stat" -> {
        println(
          statTemplate.format(
            "Id",
            "User name",
            "MON",
            "TUE",
            "WED",
            "THU",
            "FRI",
            "SAT",
            "SUN",
            "Freq at week",
            "Duration"
          )
        )
        for (stat in stats.values) {
          val meanHours = stat.visitsDuration.sumBy { it.inHours.toInt() } / stat.visitsDuration.count()
          val meanMinutes = stat.visitsDuration.sumBy { it.inMinutes.toInt() } / stat.visitsDuration.count()
          println(
            statTemplate.format(
              stat.id,
              stat.userName,
              *stat.weekStat.entries.sortedBy { it.key }.map { it.value.toString() }.toTypedArray(),
              stat.visitsAtWeek.sum() / stat.visitsAtWeek.count(),
              "${meanHours}h ${meanMinutes}m"
            )
          )
        }
      }
      else -> System.err.println("Bad command '$command'")
    }
  }
}