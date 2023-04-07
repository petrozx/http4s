package database_service.entity
import slick.jdbc.H2Profile.api._
import io.circe.generic.JsonCodec

@JsonCodec case class UserAttribute(
                          userAttr_id: String,
                          firstName: String,
                          surname :String,
                          secondName: Option[String],
                          mobilePhone: String,
                          email: String,
                          passport: String,
                        ) extends Serializable

final case class UserAttributeTable(tag: Tag) extends Table[UserAttribute](tag, "userAttribute") {
  def id = column[String]("userAttr_id", O.PrimaryKey, O.Unique)

  def firstName = column[String]("firstName")

  def surname = column[String]("surname")

  def secondName = column[Option[String]]("secondName")

  def mobilePhone = column[String]("mobilePhone")

  def email = column[String]("email")

  def passport = column[String]("passport")

  override def * = (id, firstName, surname, secondName, mobilePhone, email, passport).mapTo[UserAttribute]
}