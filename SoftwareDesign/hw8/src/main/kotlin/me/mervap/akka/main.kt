package me.mervap.akka

import akka.actor.*
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

class SearchAggregatorActor(
  private val engines: List<Class<out AbstractSearchActor>> =
    listOf(BingSearchActor::class.java, GoogleSearchActor::class.java)
) : UntypedAbstractActor() {

  private val resps = mutableListOf<SearchResponse>()
  private var waiting = engines.size

  override fun onReceive(message: Any?) {
    when (message) {
      is ReceiveTimeout -> printResults()
      is SearchResponse -> {
        resps.add(message)
        context.stop(context.sender)
        --waiting
        if (waiting == 0) {
          printResults()
        }
      }
      else -> {
        for (engine in engines) {
          val child = context.actorOf(Props.create(engine))
          child.tell(message, self)
        }
        context.setReceiveTimeout(Duration.create(3, TimeUnit.SECONDS))
      }
    }
  }

  private fun printResults() {
    context.parent.tell(
      buildString {
        for (i in 0 until 5) {
          for (engineResp in resps) {
            if (engineResp.links.size > i) {
              append("[${engineResp.engineName}] ${engineResp.links[i]}\n")
            }
          }
        }
        append("\n")
      },
      self
    )
    context.stop(self)
  }
}

class QueryHandlerActor : UntypedAbstractActor() {
  override fun onReceive(message: Any?) {
    if (message == "--next") {
      val query = readLine()!!
      if (query == "--exit") {
        context.system.terminate()
        return
      }
      val searcher = context.actorOf(Props.create(SearchAggregatorActor::class.java))
      searcher.tell(query, self)
    } else {
      println(message)
      self.tell("--next", ActorRef.noSender())
    }
  }
}

fun main() {
  val system = ActorSystem.create("SearchAggregator")
  val parent = system.actorOf(Props.create(QueryHandlerActor::class.java))
  parent.tell("--next", ActorRef.noSender())
}
