package database_service.entity

import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

@JsonCodec case class Company(
                  companyID: String,
                  name: String,
                  companyAttribute: Option[String],
                  contracts: Option[String]
                  ) extends Serializable

final case class CompanyTable(tag: Tag) extends Table[Company](tag, "company") {
  def id = column[String]("companyID", O.PrimaryKey, O.Unique)

  def name = column[String]("name")

  def companyAttribute = column[Option[String]]("companyAttribute")

  def contracts = column[Option[String]]("contracts")

  def companyAttributeRef = foreignKey("companyAttribute", companyAttribute, TableQuery[CompanyAttributeTable])(_.id)
  override def * : ProvenShape[Company] = (id, name, companyAttribute, contracts).mapTo[Company]
}

