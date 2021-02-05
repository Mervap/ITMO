package me.mervap.utils

import org.w3c.xhr.XMLHttpRequest

fun get(url: String, configure: XMLHttpRequest.() -> Unit = {}) {
  val http = XMLHttpRequest()
  http.open("GET", url)
  http.withCredentials = true
  http.configure()
  http.send()
}

fun post(url: String, data: dynamic, configure: XMLHttpRequest.() -> Unit = {}) {
  val http = XMLHttpRequest()
  http.open("POST", url)
  http.withCredentials = true
  http.configure()
  http.send(data)
}