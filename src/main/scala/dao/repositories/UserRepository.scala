package dao.repositories
import config.util.HikariUtils.db
import dao.entity.{User, UserAttribute}
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

object UserRepository {

  final case class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[String]("id", O.PrimaryKey, O.Unique)
    def username = column[String]("username", O.Unique)
    def password = column[String]("password")
    def role = column[String]("role")
    def userAttributeId = column[Option[String]]("userAttributeId")
    override def *  = (id, username, password, role, userAttributeId).mapTo[User]
    def userAttribute = foreignKey("userAttribute", userAttributeId, userAttributes)(_.id)
  }

  final case class UserAttributeTable(tag: Tag) extends Table[UserAttribute](tag, "userAttribute") {
    def id = column[String]("id", O.PrimaryKey, O.Unique)
    def firstName = column[String]("firstName")
    def surname = column[String]("surname")
    def secondName = column[Option[String]]("secondName")
    def mobilePhone = column[String]("mobilePhone")
    def email = column[String]("email")
    def passport = column[String]("passport")
    override def * : ProvenShape[UserAttribute] = (id, firstName, surname, secondName, mobilePhone, email, passport).mapTo[UserAttribute]
  }

  lazy val users: TableQuery[UserTable] = TableQuery[UserTable]
  lazy val userAttributes: TableQuery[UserAttributeTable] = TableQuery[UserAttributeTable]

  def takeUser() = db.run(
      users.result
    )

  def findUserByUsername(username: String) =
    db.run(users.filter(_.username === username).result.headOption)


}
