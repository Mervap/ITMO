package me.mervap

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger
import java.security.MessageDigest

object DbSettings {

  val db by lazy {
    val db = Database.connect(
      "jdbc:postgresql://localhost:5432/kcs2020",
      driver = "org.postgresql.Driver",
      user = "",
      password = ""
    )

    transaction {
      SchemaUtils.create(UsersTable)
      SchemaUtils.create(PlotDataTable)
    }
    db
  }
}

object UsersTable : IntIdTable() {
  val name = varchar("name", 50).uniqueIndex()
  val password = char("password", 256)
}

object PlotDataTable : IntIdTable() {
  val user_id = integer("user_id").references(UsersTable.id)
  val data = blob("plot_data")
  val save_date = datetime("save_date")
}

fun encryptPassword(password: String): String {
  val md = MessageDigest.getInstance("SHA-256")
  val salted = password + "sHnm5SjKa4342"
  return String.format("%064x%n", BigInteger(1, md.digest(salted.toByteArray())))
}