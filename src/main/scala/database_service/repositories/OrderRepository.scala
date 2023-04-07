package database_service.repositories

import config.util.HikariUtils.db
import config.util.Token
import database_service.entity.{Order, OrderTable}
import database_service.repositories.CompanyRepository.userCompaniesCollectionTable
import database_service.repositories.TypeOfWorkRepository.typeOfWorkTable
import slick.jdbc.PostgresProfile.api._

object OrderRepository {

  val orderTable = TableQuery[OrderTable]

  def saveOrder(order: Order) = db.run{
    orderTable += order
  }

  def takeOrdersByTokens(token: Token) = db.run{
    if (token.role=="admin") (for {
      ordersColl <- userCompaniesCollectionTable
      orders <- orderTable if orders.companyId === ordersColl.companyId
      typeOfWork <- typeOfWorkTable if orders.typeOfWork === typeOfWork.id
    } yield (orders, typeOfWork)).result
     else (for {
        ordersColl <- userCompaniesCollectionTable if ordersColl.userId === token.userId
        orders <- orderTable if orders.companyId === ordersColl.companyId
        typeOfWork <- typeOfWorkTable if orders.typeOfWork === typeOfWork.id
    } yield (orders, typeOfWork)).result
  }

  def updateNewOrders(token: Token) = db.run {
    orderTable.filter(_.status==="new").map(_.status).update(Some("seen"))
  }
}
