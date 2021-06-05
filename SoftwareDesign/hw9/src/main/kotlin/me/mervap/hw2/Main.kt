import io.netty.handler.codec.http.HttpMethod
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServer
import me.mervap.hw2.handlers.GetMethodsHandler
import me.mervap.hw2.handlers.PostMethodsHandler

fun main() {
  HttpServer
    .newServer(8080)
    .start { req, resp ->
      when (req.httpMethod) {
        HttpMethod.GET -> GetMethodsHandler.handle(req, resp)
        HttpMethod.POST -> PostMethodsHandler.handle(req, resp)
        else -> resp.setStatus(HttpResponseStatus.NOT_FOUND)
      }
    }
    .awaitShutdown()
}