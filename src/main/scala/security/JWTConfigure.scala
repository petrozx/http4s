package security

import config.util.Token
import io.circe.syntax.EncoderOps
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import java.time.Clock

object JWTConfigure {

  private val SECRET_KEY = "petroz"

  implicit val clock: Clock = Clock.systemUTC()

  def jwtEncode(token: Token): String = {
    val json = token.asJson.toString()
    val claim = JwtClaim {
      json
    }.issuedNow.expiresIn(86400)
    Jwt.encode(claim, SECRET_KEY, JwtAlgorithm.HS512)
  }

  def jwtDecode(token: String): Option[JwtClaim] = {
    Jwt.decode(token, SECRET_KEY, Seq(JwtAlgorithm.HS512)).toOption
  }

}
