package me.mervap.manager.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
  val env = applicationEngineEnvironment {
    module { main() }
    connector {
      port = 8080
    }
  }
  embeddedServer(Netty, env).start(true)
}