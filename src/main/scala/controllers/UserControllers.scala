package controllers

import at.favre.lib.crypto.bcrypt.BCrypt
import cats.effect.IO
import config.util.Token
import dao.entity._
import dao.repositories.UserRepository.findUserByUsername
import io.circe.jawn
import io.circe.literal.JsonStringContext
import org.http4s.Method.{GET, POST}
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._
import org.http4s.{Header, HttpRoutes, Response, Status}
import org.typelevel.ci.CIStringSyntax
import security.JWTConfigure.{jwtDecode, jwtEncode}

object UserControllers {

  val userControllers: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> Root / "login" => for {
      login <- req.as[Login]
      user <- IO.fromFuture(IO.delay(findUserByUsername(login.username)))
      _ <- IO.pure(if (!BCrypt.verifyer().verify(login.password.toCharArray, user.get.password).verified) {
        Response[IO](Status.Forbidden)
      })
      token <- IO.pure(Token(user.get.id, user.get.role))
      jwt <- IO.pure(jwtEncode(token))
      header <- IO.pure(Header.Raw(ci"jwt-token", jwt))
      resp <- Ok(json"""{"role": ${token.role}}""").map(_.putHeaders(header))
    } yield resp

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

}
