package database_service.entity
import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape
@JsonCodec case class CompanyAttribute(
                           id: String,
                           inn: String,
                           ogrn: String
                           ) extends Serializable

final case class CompanyAttributeTable(tag: Tag) extends Table[CompanyAttribute](tag, "companyAttribute") {
  def id = column[String]("id", O.PrimaryKey, O.Unique)
  def inn = column[String]("inn")
  def ogrn = column[String]("ogrn")
  override def * : ProvenShape[CompanyAttribute] = (id, inn, ogrn).mapTo[CompanyAttribute]
}