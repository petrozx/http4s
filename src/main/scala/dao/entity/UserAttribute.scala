package dao.entity

import io.circe.generic.JsonCodec

@JsonCodec case class UserAttribute(
                          id: String,
                          firstName: String,
                          surname :String,
                          secondName: Option[String],
                          mobilePhone: String,
                          email: String,
                          passport: String,
                        ) extends Serializable
