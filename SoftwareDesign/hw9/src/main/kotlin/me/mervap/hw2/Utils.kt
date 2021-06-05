package me.mervap.hw2

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.cookie.DefaultCookie
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import me.mervap.hw2.model.YePreferences

object Utils {
  val HttpServerRequest<ByteBuf>.userName: String
    get() = nullableUserName ?: "Guest"

  val HttpServerRequest<ByteBuf>.nullableUserName: String?
    get() = queryCookie("userName")

  fun HttpServerResponse<ByteBuf>.addCookies(userName: String, yePref: String) {
    addCookie(DefaultCookie("userName", userName))
    addCookie(DefaultCookie("userYePref", yePref))
  }

  val HttpServerRequest<ByteBuf>.userYePref: YePreferences
    get() = queryCookie("userYePref")?.let { YePreferences.valueOf(it) } ?: YePreferences.USD

  private fun HttpServerRequest<ByteBuf>.queryCookie(name: String): String? =
    cookies[name]?.single()?.value()?.takeIf { it.isNotEmpty() }
}