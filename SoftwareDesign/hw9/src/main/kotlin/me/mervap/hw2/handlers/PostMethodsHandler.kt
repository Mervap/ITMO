package me.mervap.hw2.handlers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import me.mervap.hw2.DB
import me.mervap.hw2.Utils.addCookies
import me.mervap.hw2.Utils.nullableUserName
import me.mervap.hw2.Utils.userYePref
import me.mervap.hw2.model.Good
import me.mervap.hw2.model.User
import rx.Observable
import java.nio.charset.Charset

object PostMethodsHandler {
  fun handle(req: HttpServerRequest<ByteBuf>, resp: HttpServerResponse<ByteBuf>): Observable<Void> {
    return when (req.decodedPath) {
      "/register" -> object : Observable<Void>({ sub ->
        sub.add(req.argsMap.subscribe {
          val login = it["login"]!!
          val password = it["password"]!!
          val yePref = it["yePref"]!!
          sub.add(DB.findUser(login).subscribe({
            resp.status = HttpResponseStatus.BAD_REQUEST
            resp.writeString(just("User with such login already exists")).unsafeSubscribe(sub)
          }) {
            DB.saveUser(User(login, password, yePref))
            resp.addCookies(login, yePref)
            resp.status = HttpResponseStatus.OK
            resp.sendHeaders().unsafeSubscribe(sub)
          })
        })
      }) {}
      "/good" -> object : Observable<Void>({ sub ->
        val userName = req.nullableUserName
        if (userName == null) {
          resp.setHeader("Location", "/")
          resp.setStatus(HttpResponseStatus.FOUND).unsafeSubscribe(sub)
        } else {
          sub.add(req.argsMap.subscribe { params ->
            val name = params["name"]!!
            val price = params["price"]!!.toDouble()
            val userYePref = req.userYePref
            DB.saveGood(Good(userName, name, userYePref.toUsd(price))).subscribe {
              resp.setHeader("Location", "/")
              resp.status = HttpResponseStatus.FOUND
              resp.sendHeaders().unsafeSubscribe(sub)
            }
          })
        }
      }) {}
      "/login" -> object : Observable<Void>({ sub ->
        sub.add(req.argsMap.subscribe { params ->
          val login = params["login"]!!
          val password = params["password"]!!
          sub.add(DB.findUser(login).subscribe({
            if (it.password == password) {
              resp.status = HttpResponseStatus.OK
              resp.addCookies(login, it.yePref.name)
              resp.sendHeaders().unsafeSubscribe(sub)
            } else {
              resp.status = HttpResponseStatus.BAD_REQUEST
              resp.writeString(just("Password doesn't matches")).unsafeSubscribe(sub)
            }
          }) {
            resp.status = HttpResponseStatus.BAD_REQUEST
            resp.writeString(just("User not found")).unsafeSubscribe(sub)
          })
        })
      }) {}
      else -> resp.setStatus(HttpResponseStatus.NOT_FOUND)
    }
  }

  private val HttpServerRequest<ByteBuf>.argsMap: Observable<Map<String, String>>
    get() = content.map { buf ->
      val rawData = buf.readCharSequence(contentLength.toInt(), Charset.defaultCharset())
      rawData.split("&").map {
        val (k, v) = it.split("=")
        k to v
      }.toMap()
    }
}
