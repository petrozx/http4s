package database_service.repositories

import config.util.HikariUtils.db
import database_service.entity.{ChangesTable, Order}
import slick.jdbc.PostgresProfile.api._

object ChangesRepository {

  val changesTableRepo = TableQuery[ChangesTable]

  def getChangesByOrders(orders: Seq[Order]) = db.run {
    val ids = orders.map(_.orderID)
    changesTableRepo.filter(_.orderId.inSet(ids)).result
  }

  def allVisible(userID : String) = db.run {
    changesTableRepo.filter(_.userId === userID).delete
  }
}
