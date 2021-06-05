package me.mervap.hw2.model

import org.bson.Document

data class Good(val sellerLogin: String, val name: String, val usdPrice: Double) {
  fun toDocument(): Document =
    Document("seller", sellerLogin).append("name", name).append("usdPrice", usdPrice)

  companion object {
    @JvmStatic
    fun fromDocument(doc: Document): Good =
      Good(doc.getString("seller"), doc.getString("name"), doc.getDouble("usdPrice"))
  }
}