package database_service.entity
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape
case class FileCollection(
                         refId: String,
                         fileId: String
                         ) extends Serializable

final case class FileCollectionTable(tag: Tag) extends Table[FileCollection](tag, "fileCollection") {
  def refId = column[String]("refId")
  def fileId = column[String]("fileId")
  def fileRef = foreignKey("file", fileId, TableQuery[FileTable])(_.id)
  override def * : ProvenShape[FileCollection] = (refId, fileId).mapTo[FileCollection]
}
