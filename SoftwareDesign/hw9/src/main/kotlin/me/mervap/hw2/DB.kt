package me.mervap.hw2

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import me.mervap.hw2.model.Good
import me.mervap.hw2.model.User
import rx.Observable
import rx.Subscription

object DB {
  private val database = MongoClients.create("mongodb://localhost:27017").getDatabase("rxhw")
  private val users = database.getCollection("users")
  private val goods = database.getCollection("goods")

  fun saveUser(user: User): Subscription {
    return users.insertOne(user.toDocument()).subscribe()
  }

  fun findUser(login: String): Observable<User> {
    return users.find(eq("login", login)).toObservable().map { User.fromDocument(it) }.first()
  }

  fun saveGood(good: Good): Observable<Success> {
    return goods.insertOne(good.toDocument())
  }

  fun getAllGoods(): Observable<Good> {
    return goods.find().toObservable().map { Good.fromDocument(it) }
  }
}