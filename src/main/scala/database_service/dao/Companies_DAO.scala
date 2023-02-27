package database_service.dao

import cats.effect.IO
import config.util.Token
import database_service.entity.Company
import database_service.repositories.CompanyRepository
import slick.jdbc.PostgresProfile.api._

object Companies_DAO {

  def getCompaniesByRole(token: Token): IO[Seq[Company]] = {
    token.role match {
      case "admin" => IO.fromFuture(IO.delay(CompanyRepository.getCompaniesByAdmin))
      case _ => IO.fromFuture(IO.delay(CompanyRepository.getCompaniesByUser(token)))
    }
  }
}
