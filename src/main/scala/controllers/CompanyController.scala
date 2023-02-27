package controllers

import cats.effect.IO
import cats.effect.std.UUIDGen
import config.util.Token
import database_service.dao.Companies_DAO.getCompaniesByRole
import database_service.entity.Company._
import database_service.entity._
import database_service.repositories.TypeOfWorkRepository.{getTypeOfWork, newTypeOfWork, removeTypeOfWork}
import org.http4s.AuthedRoutes
import org.http4s.Method.{GET, POST}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._
object CompanyController {

  val companyController: AuthedRoutes[Token, IO] = AuthedRoutes.of[Token, IO] {
    case GET -> Root / "companies" as token => for {
      comp <- getCompaniesByRole(token)
      res <- Ok(comp)
    } yield res

    case req@POST -> Root / "typeWork" / "add" as _ => for {
      typeOfWork <- req.req.as[TypeOfWork]
      uuid <- UUIDGen.randomString[IO]
      typeOfWorkForSave <- IO.pure(TypeOfWork(uuid, typeOfWork.name))
      _ <- IO.fromFuture(IO.delay(newTypeOfWork(typeOfWorkForSave)))
      resp <- Ok(typeOfWorkForSave)
    } yield resp

    case GET -> Root / "typeWork" / "get" as _ => for {
      typeOfWorks <- IO.fromFuture(IO.delay(getTypeOfWork))
      resp <- Ok(typeOfWorks)
    } yield resp

    case DELETE -> Root / "typeWork" / "remove" / id as _ => for {
      del <- IO.fromFuture(IO.delay(removeTypeOfWork(id)))
      resp <- Ok(del)
    } yield resp
  }
}
