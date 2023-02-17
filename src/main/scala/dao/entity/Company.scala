package dao.entity

import io.circe.generic.JsonCodec

@JsonCodec case class Company(
                  id: String,
                  name: String,
                  companyAttribute: String,
                  contracts: Option[String]
                  ) extends Serializable
@JsonCodec case class UserCompaniesCollection(
                                    userId: String,
                                    companyId: String
                                  ) extends Serializable