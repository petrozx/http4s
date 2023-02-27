package database_service.repositories
import config.util.HikariUtils.db
import database_service.entity.{UserAttributeTable, UserTable}
import slick.jdbc.H2Profile.api._

object UserRepository {



  lazy val users: TableQuery[UserTable] = TableQuery[UserTable]
  lazy val userAttributes: TableQuery[UserAttributeTable] = TableQuery[UserAttributeTable]

  def takeUser = db.run(
      users.result
    )

  def findUserByUsername(username: String) =
    db.run(users.filter(_.username === username).result.headOption)


}
