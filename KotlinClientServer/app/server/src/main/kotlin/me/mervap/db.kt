package me.mervap

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigInteger
import java.security.MessageDigest

object DbSettings {

  private lateinit var dbHost: String
  private lateinit var dbUser: String
  private lateinit var dbPassword: String

  fun initiateDbInfo(dbHost: String, dbUser: String, dbPassword: String) {
    if (this::dbHost.isInitialized) {
      throw RuntimeException("Database credentials already initiated")
    }
    this.dbHost = dbHost
    this.dbUser = dbUser
    this.dbPassword = dbPassword
  }

  val db by lazy {
    if (!this::dbHost.isInitialized) {
      throw RuntimeException("Database credentials not yet initiated")
    }
    val db = Database.connect(
      "jdbc:postgresql://$dbHost:5432/kcs_db",
      driver = "org.postgresql.Driver",
      user = dbUser,
      password = dbPassword
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