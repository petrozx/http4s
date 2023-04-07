package database_service.entity
import slick.jdbc.H2Profile.api._
import io.circe.generic.JsonCodec
import slick.lifted.ProvenShape

@JsonCodec case class Changes(
                    userId: String,
                    orderId: String,
                    field: String
                  ) extends Serializable

final case class ChangesTable(tag: Tag) extends Table[Changes](tag, "changes") {
  def userId = column[String]("user_id")
  def orderId = column[String]("order_id")
  def field = column[String]("field")
  def userIdRef = foreignKey("changes_user_user_id_fk", userId, TableQuery[UserTable])(_.id)
  def orderIdRef = foreignKey("changes_order_orderid_fk", orderId, TableQuery[OrderTable])(_.id)
  override def * : ProvenShape[Changes] = (userId, orderId, field).mapTo[Changes]
}