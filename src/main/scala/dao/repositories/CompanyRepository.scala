package dao.repositories
import config.util.HikariUtils.db
import config.util.Token
import dao.entity.{Company, UserCompaniesCollection}
import dao.repositories.UserRepository.users
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

object CompanyRepository {

  final case class CompanyTable(tag: Tag) extends Table[Company](tag, "company") {
    def id = column[String]("id", O.PrimaryKey, O.Unique)
    def name = column[String]("name")
    def companyAttribute = column[String]("companyAttribute")
    def contracts = column[Option[String]]("contracts")

    override def * : ProvenShape[Company] = (id, name, companyAttribute, contracts).mapTo[Company]
  }
  final case class UserCompaniesCollectionTable(tag: Tag) extends
    Table[UserCompaniesCollection](tag, "userCompaniesCollection") {
    def userId = column[String]("userId")
    def companyId = column[String]("companyId")
    def userRef = foreignKey("user", userId, users)(_.id)
    def companyRef = foreignKey("company", companyId, companyTable)(_.id)
    override def * : ProvenShape[UserCompaniesCollection] = (userId, companyId).mapTo[UserCompaniesCollection]
  }

  val companyTable = TableQuery[CompanyTable]
  val userCompaniesCollectionTable = TableQuery[UserCompaniesCollectionTable]

  def getCompaniesByUser(token: Token) = db.run {
    userCompaniesCollectionTable.filter(_.userId === token.userId).flatMap(_.companyRef).result
  }

  def getCompaniesbyAdmin() = db.run {
    companyTable.result
  }


}
