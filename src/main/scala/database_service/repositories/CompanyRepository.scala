package database_service.repositories
import config.util.HikariUtils.db
import config.util.Token
import database_service.entity.{CompaniesIDs, Company, CompanyAttribute, CompanyAttributeTable, CompanyTable, UserCompaniesCollection, UserCompaniesCollectionTable}
import slick.jdbc.PostgresProfile.api._

object CompanyRepository {

  val companyTable = TableQuery[CompanyTable]

  val companyAttributeTable = TableQuery[CompanyAttributeTable]

  val userCompaniesCollectionTable = TableQuery[UserCompaniesCollectionTable]

  def saveUserCompaniesCollection(uuidUser:String, uuidsComp: CompaniesIDs) = db.run {
    userCompaniesCollectionTable ++= uuidsComp.companyId.map(UserCompaniesCollection(uuidUser, _))
  }

  def getCompaniesByUser(token: Token) = db.run {
    userCompaniesCollectionTable.filter(_.userId === token.userId).flatMap(_.companyRef).result
  }
  def getCompaniesByAdmin = db.run {
    companyTable.result
  }

  def saveCompanyAttribute(companyAttribute: CompanyAttribute) = db.run {
    companyAttributeTable += companyAttribute
  }

  def saveCompany(company: Company) = db.run {
    companyTable += company
  }

  def deleteUseCompaniesCollection(uuidUser:String) = db.run{
    userCompaniesCollectionTable.filter(_.userId===uuidUser).delete
  }
}

