package database_service.entity

import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

@JsonCodec case class File(
               id: String,
               name: String,
               url: String
               ) extends Serializable

final case class FileTable(tag: Tag) extends Table[File](tag, "file") {
  def id = column[String]("id", O.PrimaryKey, O.Unique)
  def name = column[String]("name")
  def url = column[String]("url")
  override def * : ProvenShape[File] = (id, name, url).mapTo[File]
}
