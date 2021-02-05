package me.mervap

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.mervap.hashtag.MAX_COUNT_PER_QUERY
import me.mervap.hashtag.MAX_QUERIES_CNT
import me.mervap.hashtag.VKHashtagAnalyzer
import me.mervap.time.RoundStage
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

fun Application.main() {

  install(CORS) {
    anyHost()
    allowCredentials = true
    allowNonSimpleContentTypes = true
  }

  install(Sessions) {
    cookie<UserIdPrincipal>("ktor_session_cookie", SessionStorageMemory())
  }

  install(Authentication) {
    session<UserIdPrincipal> {
      challenge {
        call.respond(HttpStatusCode.Unauthorized)
      }
      validate { it }
    }
  }

  routing {

    static {
      resources("/")
      defaultResource("index.html", "/")
      resource("login", "index.html")
      resource("registration", "index.html")
      resource("storage", "index.html")
    }

    post("/login") {
      try {
        val (username, password) = Json.decodeFromString<UserCredentials>(call.receiveText())
        val userExists = transaction(DbSettings.db) {
          val user =
            UsersTable.select { (UsersTable.name eq username) and (UsersTable.password eq encryptPassword(password)) }
          user.firstOrNull() != null
        }
        if (userExists) {
          call.sessions.set(UserIdPrincipal(username))
          call.respondRedirect("/")
        }
        else {
          call.respond(HttpStatusCode.Unauthorized, "User not found. Check credentials")
        }
      }
      catch (e: RuntimeException) {
        call.respond(HttpStatusCode.BadRequest)
      }
    }

    get("/logout") {
      call.sessions.clear<UserIdPrincipal>()
      call.respondRedirect("/")
    }

    post("/registration") {
      try {
        val (username, password) = Json.decodeFromString<UserCredentials>(call.receiveText())
        val userExists = transaction(DbSettings.db) {
          val user = UsersTable.select { UsersTable.name eq username }
          user.firstOrNull() != null
        }
        if (userExists) {
          call.respond(HttpStatusCode.Unauthorized, "User '$username' already exists")
          return@post
        }

        transaction {
          UsersTable.insert {
            it[this.name] = username
            it[this.password] = encryptPassword(password)
          }
        }
        call.sessions.set(UserIdPrincipal(username))
        call.respondRedirect("/")
      }
      catch (e: RuntimeException) {
        call.respond(HttpStatusCode.BadRequest)
      }
    }

    get("/hashtag_stat") {
      val params = call.request.queryParameters
      val hashtag = params["hashtag"]
      val searchInterval = params["search_interval"]?.let { SearchInterval.valueOf(it) }
      val hours = searchInterval?.hours
      if (hashtag == null || hours == null || hours < 1) {
        call.respond(HttpStatusCode.BadRequest)
        return@get
      }
      val infos = VKHashtagAnalyzer.getHashtagStatisticByLastNHours(hashtag, hours)
      val x = mutableListOf<String>()
      val y = mutableListOf<Int>()
      var sum = 0
      for ((interval, cnt) in infos) {
        val diff = interval.end - interval.start
        val middle = interval.start + diff
        val nextX = when {
          diff.seconds < 60 * 6 -> middle.dateTimeString(RoundStage.NONE)
          diff.seconds < 60 * 60 -> middle.dateTimeString(RoundStage.LAST_MINUTE)
          else -> middle.dateTimeString(RoundStage.ZERO_MINUTE)
        }
        x.add(nextX)
        y.add(cnt)
        sum += cnt
      }
      val isOverflow = sum == MAX_COUNT_PER_QUERY * MAX_QUERIES_CNT
      val resY = if (isOverflow) y.dropWhile { it == 0 } else y
      val resX = if (isOverflow) x.takeLast(resY.size) else x
      call.respond(Json.encodeToString(PlotData(resX, resY, isOverflow, hashtag, searchInterval)))
    }

    authenticate {
      get("/get_username") {
        val principal = call.authentication.principal<UserIdPrincipal>()
        if (principal != null) {
          call.respond(principal.name)
        }
        else {
          call.respond(HttpStatusCode.Unauthorized)
        }
      }

      get("/get_saved_plot") {
        val principal = call.authentication.principal<UserIdPrincipal>()
        if (principal == null) {
          call.respond(HttpStatusCode.Unauthorized)
          return@get
        }

        val plots = transaction {
          (UsersTable innerJoin PlotDataTable)
            .select { UsersTable.name eq principal.name }
            .orderBy(PlotDataTable.save_date, SortOrder.DESC)
            .map {
              // SavedPlot
              buildString {
                append("{\"plotData\":")
                append(String(it[PlotDataTable.data].bytes))
                append(", ")
                append("\"saveTime\":\"")
                append(it[PlotDataTable.save_date].toString("d MMMM yyyy, HH:mm"))
                append("\"}")
              }
            }
        }
        call.respond(plots.joinToString(prefix = "[", postfix = "]", separator = ", "))
      }

      post("/save_plot") {
        val principal = call.authentication.principal<UserIdPrincipal>()
        if (principal == null) {
          call.respond(HttpStatusCode.Unauthorized)
          return@post
        }

        val data = call.receiveText()
        transaction {
          val id = UsersTable.select { UsersTable.name eq principal.name }.first()[UsersTable.id].value
          PlotDataTable.insert {
            it[this.user_id] = id
            it[this.data] = ExposedBlob(data.toByteArray())
            it[this.save_date] = DateTime.now()
          }
        }
        call.respond(HttpStatusCode.OK)
      }
    }
  }
}