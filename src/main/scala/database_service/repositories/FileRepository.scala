package database_service.repositories
import config.util.HikariUtils.db
import database_service.entity.{File, FileCollection, FileCollectionTable, FileTable}
import slick.jdbc.PostgresProfile.api._

object FileRepository {

  val fileRepository = TableQuery[FileTable]
  val fileCollection = TableQuery[FileCollectionTable]

  def saveFile(file: File, id: String) = db.run{
    fileRepository += file
    fileCollection += FileCollection(id, file.id)
  }

  def takeFiles(id: String) = db.run{
    fileCollection.filter(_.fileId === id).flatMap(_.fileRef).result
  }
}
