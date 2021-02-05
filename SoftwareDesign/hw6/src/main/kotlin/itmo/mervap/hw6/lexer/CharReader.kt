package itmo.mervap.hw6.lexer

import java.io.InputStream

class CharReader(private val inputStream: InputStream) {
  var curChar: Char? = null
    private set
  var eof: Boolean = false
    private set

  fun readNext() {
    if (eof) return
    val next = inputStream.read()
    if (next == -1) {
      eof = true
      curChar = null
    } else {
      curChar = next.toChar()
      if (curChar == '\n') {
        eof = true
        curChar = null
      }
    }
  }
}