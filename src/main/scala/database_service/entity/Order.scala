package database_service.entity
import io.circe.generic.JsonCodec
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

import java.time.LocalDateTime
@JsonCodec case class Order(
                  id: String,
                  typeOfWork: String,
                  date: Option[LocalDateTime],
                  announcedDateOfWork: Option[LocalDateTime],
                  factDateOfWork: Option[LocalDateTime],
                  numberOfSeal: String,
                  natureOfCargo: String,
                  weight: String,
                  containerType: String,
                  numberContainer: String,
                  sizeContainer: String,
                  specialMarks: String,
                  companyId: String
                ) extends Serializable

final case class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
  def id = column[String]("id", O.PrimaryKey, O.Unique)
  def typeOfWork = column[String]("typeOfWork")
  def date = column[Option[LocalDateTime]]("date")
  def announcedDateOfWork = column[Option[LocalDateTime]]("announcedDateOfWork")
  def factDateOfWork = column[Option[LocalDateTime]]("factDateOfWork")
  def numberOfSeal = column[String]("numberOfSeal")
  def natureOfCargo = column[String]("natureOfCargo")
  def weight = column[String]("weight")
  def containerType = column[String]("containerType")
  def numberContainer = column[String]("numberContainer")
  def sizeContainer = column[String]("sizeContainer")
  def specialMarks = column[String]("specialMarks")
  def companyId = column[String]("companyId")
  def typeOfWorkRef = foreignKey("typeOfWork", typeOfWork, TableQuery[TypeOfWorkTable])(_.id)
  override def * : ProvenShape[Order] = (id, typeOfWork, date, announcedDateOfWork, factDateOfWork,
    numberOfSeal, natureOfCargo, weight, containerType, numberContainer, sizeContainer, specialMarks,
    companyId).mapTo[Order]
}
