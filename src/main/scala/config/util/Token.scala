package config.util

import io.circe.generic.JsonCodec

@JsonCodec case class Token(userId: String, role: String)
