package me.mervap.manager.client.utils

import org.w3c.xhr.XMLHttpRequest

internal fun get(url: String, configure: XMLHttpRequest.() -> Unit = {}) {
  val http = XMLHttpRequest()
  http.open("GET", url)
  http.configure()
  http.send()
}

internal fun post(url: String, data: dynamic, configure: XMLHttpRequest.() -> Unit = {}) {
  val http = XMLHttpRequest()
  http.open("POST", url)
  http.configure()
  http.send(data)
}