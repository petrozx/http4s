package config.util

import slick.jdbc.JdbcBackend.Database

object HikariUtils {
  val db = Database.forConfig("db")
}
