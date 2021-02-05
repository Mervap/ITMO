package me.mervap

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

fun main(args: Array<String>) {
  val argParser = ArgParser("server")
  val host by argParser.option(ArgType.String, "host")
  val port by argParser.option(ArgType.Int, "port")
  val dbHost by argParser.option(ArgType.String, "dbHost")
  val dbUser by argParser.option(ArgType.String, "dbUser")
  val dbPassword by argParser.option(ArgType.String, "dbPassword")
  argParser.parse(args)

  DbSettings.initiateDbInfo(dbHost!!, dbUser!!, dbPassword!!)

  val env = applicationEngineEnvironment {
    module {
      main()
    }
    connector {
      this.host = host!!
      this.port = port!!
    }
  }
  embeddedServer(Netty, env).start(true)
}