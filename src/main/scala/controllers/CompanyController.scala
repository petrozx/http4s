package controllers

import cats.effect.IO
import config.util.Token
import dao.entity.Company._
import dao.repositories.CompanyRepository.{getCompaniesByUser, getCompaniesbyAdmin}
import org.http4s.AuthedRoutes
import org.http4s.Method.GET
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._

object CompanyController {

  val companyController = AuthedRoutes.of[Token, IO] {
    case GET -> Root / "companies" as token => for {
      comp <- token.role match {
        case "admin" => IO.fromFuture(IO.delay(getCompaniesbyAdmin()))
        case _ => IO.fromFuture(IO.delay(getCompaniesByUser(token)))
      }
      res <- Ok(comp)
    } yield res
  }
}
