package me.mervap.hw2.handlers

import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.cookie.DefaultCookie
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.ResponseContentWriter
import me.mervap.hw2.DB
import me.mervap.hw2.ResourceManager
import me.mervap.hw2.Utils.addCookies
import me.mervap.hw2.Utils.nullableUserName
import me.mervap.hw2.Utils.userName
import me.mervap.hw2.Utils.userYePref

object GetMethodsHandler {
  fun handle(req: HttpServerRequest<ByteBuf>, resp: HttpServerResponse<ByteBuf>): ResponseContentWriter<ByteBuf> {
    if (req.decodedPath.startsWith("/css")) {
      return resp.writeString(ResourceManager.loadResource(req.decodedPath.drop(1)))
    }
    return when (req.decodedPath) {
      "/" -> resp.writeString(ResourceManager.indexPage.zipWith(DB.getAllGoods().toList()) { page, goods ->
        val yePref = req.userYePref
        val tableData = goods.joinToString("\n") {
          """
            <tr>
              <td>${it.sellerLogin}</td>
              <td>${it.name}</td>
              <td>${String.format("%.2f", yePref.fromUsd(it.usdPrice))} ${yePref.name}</td>
            </tr>
          """.trimIndent()
        }
        page.replace(
          "<<-- SIDEBAR PLACEHOLDER -->>",
          if (req.nullableUserName == null) SIDEBAR_WITHOUT_COOKIES
          else SIDEBAR_WITH_COOKIES
        )
          .replace("<<-- USER PLACEHOLDER -->>", req.userName)
          .replace("<<-- DATA PLACEHOLDER -->>", tableData)
      })
      "/good" -> resp.writeString(ResourceManager.addGoodPage)
      "/register" -> resp.writeString(ResourceManager.registerPage)
      "/login" -> resp.writeString(ResourceManager.loginPage)
      "/logout" -> {
        resp.addCookies("", "")
        resp.setHeader("Location", "/")
        resp.setStatus(HttpResponseStatus.FOUND)
      }
      else -> {
        resp.setHeader("Location", "/")
        resp.setStatus(HttpResponseStatus.FOUND)
      }
    }
  }

  private val SIDEBAR_WITHOUT_COOKIES = """
    <div><a href="/login">Sign in</a></div>
    <div><a href="/register">Sign up</a></div>
  """.trimIndent()

  private val SIDEBAR_WITH_COOKIES = """
    <div><a href="/good">Add good</a></div>
    <div><a href="/logout">Log out</a></div>
  """.trimIndent()
}
