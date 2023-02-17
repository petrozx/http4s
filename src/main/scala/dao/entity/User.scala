package dao.entity

import io.circe.generic.JsonCodec

@JsonCodec case class User(
                id: String,
                username: String,
                password: String,
                role: String,
                userAttributeId: Option[String]
                ) extends Serializable

@JsonCodec case class Login(
                             username: String,
                             password: String,
                           )