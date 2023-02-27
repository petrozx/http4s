package database_service.repositories
import config.util.HikariUtils.db
import config.util.Token
import database_service.entity.{CompanyTable, UserCompaniesCollectionTable}
import slick.jdbc.PostgresProfile.api._

object CompanyRepository {

  private val companyTable = TableQuery[CompanyTable]

  private val userCompaniesCollectionTable = TableQuery[UserCompaniesCollectionTable]

  def getCompaniesByUser(token: Token) = db.run {
    userCompaniesCollectionTable.filter(_.userId === token.userId).flatMap(_.companyRef).result
  }
  def getCompaniesByAdmin = db.run {
    companyTable.result
  }
}

