package controllers

import at.favre.lib.crypto.bcrypt.BCrypt
import cats.effect.IO
import cats.effect.std.UUIDGen
import config.util.{Login, Token}
import database_service.entity.{CompaniesIDs, User, UserAttribute}
import database_service.repositories.CompanyRepository.{deleteUseCompaniesCollection, saveUserCompaniesCollection}
import database_service.repositories.UserRepository._
import io.circe.jawn
import io.circe.literal.JsonStringContext
import org.http4s.Method.{GET, POST}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._
import org.http4s.{AuthedRoutes, Header, HttpRoutes, Status}
import org.typelevel.ci.CIStringSyntax
import security.JWTConfigure.{jwtDecode, jwtEncode}

object UserControllers {

  val userControllers: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "login" => for {
      login <- req.as[Login]
      user <- IO.fromFuture(IO.delay(findUserByUsername(login.username)))
      response <- if (!BCrypt.verifyer().verify(login.password.toCharArray, user.get.password).verified) {
        Forbidden()
      } else for {
          token <- IO.pure(Token(user.get.user_id, user.get.role))
          jwt <- IO.pure(jwtEncode(token))
          header <- IO.pure(Header.Raw(ci"jwt-token", jwt))
          resp <- Ok(json"""{"role": ${token.role}}""").map(_.putHeaders(header))
        } yield resp
    } yield response

    case req@GET -> Root / "verify" => for {
      token <- IO.pure(req.headers.get(ci"jwt-token"))
      verify <- IO.pure(jwtDecode(token.get.head.value))
      tokenInfo <- IO.fromEither(jawn.decode[Token](verify.get.content))
      _ <- IO.pure(verify match {
        case Some(value) => Status.Ok
        case None => Status.Forbidden
      })
      resp <- Ok(json"""{"role": ${tokenInfo.role}}""")
    } yield resp
  }

  val authUserController: AuthedRoutes[Token, IO] = AuthedRoutes.of[Token, IO]{
    case req@POST -> Root / "user" / "register" as _ => for {
      uuid1 <- UUIDGen.randomString[IO]
      uuid2 <- UUIDGen.randomString[IO]
      userAttribute <- req.req.as[UserAttribute].flatMap(ua => IO.pure(ua.copy(userAttr_id=uuid1)))
      user <- req.req.as[User].flatMap(u => IO.pure(u.copy(user_id=uuid2, userAttributeId=Some(uuid1))))
      companiesIds <- req.req.as[CompaniesIDs]
      _ <- IO.fromFuture(IO(saveUserAttribute(userAttribute)))
      _ <- IO.fromFuture(IO(saveUser(user)))
      _ <- IO.fromFuture(IO(saveUserCompaniesCollection(uuid2, companiesIds)))
      resp <- Ok(user)
    } yield resp

    case req@PUT -> Root / "user" as _ => for {
      user <- req.req.as[User]
      companiesIds <- req.req.as[CompaniesIDs]
      userAttribute <- req.req.as[UserAttribute]
      _ <- IO.fromFuture(IO(updateUser(user)))
      _ <- IO.fromFuture(IO(updateUserAttribute(userAttribute)))
      _ <- IO.fromFuture(IO(deleteUseCompaniesCollection(user.user_id)))
      _ <- IO.fromFuture(IO(saveUserCompaniesCollection(user.user_id, companiesIds)))
      resp <- Ok(Status.AlreadyReported.code)
    } yield resp

    case GET -> Root / "users" as token => for {
      users <- IO.fromFuture(IO(getUsers()))
      companies <- IO.fromFuture(IO(getCompaniesIDs()))
      userComp <- IO(users.map(u => (u._1, u._2, companies.filter(c => u._1.user_id == c.userId).map(_.companyId))))
      resp <- Ok(userComp)
    } yield resp
  }

}
