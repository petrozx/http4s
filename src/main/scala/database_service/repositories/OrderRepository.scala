package database_service.repositories

import config.util.HikariUtils.db
import database_service.entity.{Order, OrderTable}
import slick.jdbc.PostgresProfile.api._

object OrderRepository {

  val orderTable = TableQuery[OrderTable]

  def saveOrder(order: Order) = db.run{
    orderTable += order
  }
}
