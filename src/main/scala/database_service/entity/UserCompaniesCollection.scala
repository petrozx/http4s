package database_service.entity
import slick.jdbc.H2Profile.api._

final case class UserCompaniesCollectionTable(tag: Tag) extends
  Table[(String, String)](tag, "userCompaniesCollection") {
  def userId = column[String]("userId")

  def companyId = column[String]("companyId")

  def pk = primaryKey("pk_enrollment", (userId, companyId))

  def userRef = foreignKey("userId", userId, TableQuery[UserTable])(_.id)

  def companyRef = foreignKey("companyId", companyId, TableQuery[CompanyTable])(_.id)

  override def * = (userId, companyId)
}
