package database_service.entity
import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._

@JsonCodec case class CompaniesIDs(companyId: List[String])

@JsonCodec case class UserCompaniesCollection(
                                    userId: String,
                                    companyId: String,
                                  ) extends Serializable

final case class UserCompaniesCollectionTable(tag: Tag) extends
  Table[UserCompaniesCollection](tag, "userCompaniesCollection") {
  def userId = column[String]("userId")

  def companyId = column[String]("companyId")

  def pk = primaryKey("pk_enrollment", (userId, companyId))

  def userRef = foreignKey("userId", userId, TableQuery[UserTable])(_.id)

  def companyRef = foreignKey("companyId", companyId, TableQuery[CompanyTable])(_.id)

  override def * = (userId, companyId).mapTo[UserCompaniesCollection]
}
