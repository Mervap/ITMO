package me.mervap.hw2.model

import org.bson.Document

enum class YePreferences {
  USD,
  EUR,
  RUB;

  fun fromUsd(price: Double) = when (this) {
    USD -> price
    EUR -> 0.77 * price
    RUB -> 75 * price
  }

  fun toUsd(price: Double) = when (this) {
    USD -> price
    EUR -> price / 0.77
    RUB -> price / 75
  }
}

data class User(val login: String, val password: String, val yePref: YePreferences) {

  constructor(login: String, password: String, yePref: String) :
      this(login, password, YePreferences.valueOf(yePref))

  fun toDocument(): Document =
    Document("login", login).append("password", password).append("yePref", yePref.name)

  companion object {
    @JvmStatic
    fun fromDocument(doc: Document): User =
      User(doc.getString("login"), doc.getString("password"), YePreferences.valueOf(doc.getString("yePref")))
  }
}