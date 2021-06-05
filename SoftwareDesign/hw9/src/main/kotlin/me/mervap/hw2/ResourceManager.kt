package me.mervap.hw2

import rx.Observable
import java.util.concurrent.ConcurrentHashMap

object ResourceManager {

  private val resourceCache = ConcurrentHashMap<String, Observable<String>>()

  val indexPage = loadResource("index.html")
  val addGoodPage = loadResource("good.html")
  val registerPage = loadResource("register.html")
  val loginPage = loadResource("login.html")

  fun loadResource(path: String) =
    resourceCache.computeIfAbsent(path) {
      Observable.fromCallable {
        String(javaClass.classLoader.getResourceAsStream(path)?.readAllBytes() ?: error("Unknown resource $path"))
      }
    }
}