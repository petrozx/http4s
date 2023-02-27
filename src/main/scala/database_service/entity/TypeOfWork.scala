package database_service.entity
import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

@JsonCodec case class TypeOfWork(
                     id: String,
                     name: String
                     ) extends Serializable

final case class  TypeOfWorkTable(tag: Tag) extends Table[TypeOfWork](tag, "typeOfWork") {
  def id = column[String]("id", O.Unique, O.PrimaryKey)
  def name = column[String]("name", O.Unique)
  override def * : ProvenShape[TypeOfWork] = (id, name).mapTo[TypeOfWork]
}