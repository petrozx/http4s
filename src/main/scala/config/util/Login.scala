package config.util

import io.circe.generic.JsonCodec

@JsonCodec case class Login(
                             username: String,
                             password: String,
                           )
