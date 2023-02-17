package config.util

import slick.jdbc.JdbcBackend.Database

object HikariUtils {

  lazy val db = Database.forConfig("db")
}
