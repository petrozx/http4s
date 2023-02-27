package database_service.repositories

import config.util.HikariUtils.db
import database_service.entity.{TypeOfWork, TypeOfWorkTable}
import slick.jdbc.PostgresProfile.api._

object TypeOfWorkRepository {
  private val typeOfWorkTable = TableQuery[TypeOfWorkTable]
  def newTypeOfWork(typeOfWork: TypeOfWork) = db.run{
    typeOfWorkTable += typeOfWork
  }

  def getTypeOfWork = db.run {
    typeOfWorkTable.result
  }

  def removeTypeOfWork(id: String) = db.run {
    typeOfWorkTable.filter(_.id === id).delete
  }
}
