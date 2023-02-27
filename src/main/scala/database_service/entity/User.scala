package database_service.entity
import slick.jdbc.H2Profile.api._

import io.circe.generic.JsonCodec

@JsonCodec case class User(
                id: String,
                username: String,
                password: String,
                role: String,
                userAttributeId: Option[String]
                ) extends Serializable

final case class UserTable(tag: Tag) extends Table[User](tag, "user") {
  def id = column[String]("id", O.PrimaryKey, O.Unique)

  def username = column[String]("username", O.Unique)

  def password = column[String]("password")

  def role = column[String]("role")

  def userAttributeId = column[Option[String]]("userAttributeId")

  override def * = (id, username, password, role, userAttributeId).mapTo[User]

  def userAttribute = foreignKey("userAttribute", userAttributeId, TableQuery[UserAttributeTable])(_.id)
}
