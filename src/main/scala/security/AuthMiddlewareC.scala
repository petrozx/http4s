package security

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import config.util.Token
import io.circe.jawn
import org.http4s.dsl.io.Ok
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRequest, Request, Response, Status}
import org.typelevel.ci.CIStringSyntax
import security.JWTConfigure.jwtDecode

object AuthMiddlewareC {


  def authVerify: AuthMiddleware[IO, Token] =
    routes => Kleisli { req: Request[IO] =>
        req.headers.get(ci"jwt-token") match {
          case Some(value) => jwtDecode(value.head.value) match {
                case Some(value) => jawn.decode[Token](value.content) match {
                  case Left(_) => OptionT.liftF(IO.pure(Response(Status.Forbidden)))
                  case Right(value) => routes(AuthedRequest(value, req))
                }
                case None => OptionT.liftF(IO.pure(Response(Status.Forbidden)))
              }
          case None => OptionT.liftF(IO.pure(Response(Status.Forbidden)))
        }
    }
}
