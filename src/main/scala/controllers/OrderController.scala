package controllers

import cats.effect.IO
import cats.effect.std.UUIDGen
import config.util.Token
import database_service.entity.Order
import database_service.repositories.OrderRepository.saveOrder
import org.http4s.AuthedRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.dsl.io._

object OrderController {

  val orderController = AuthedRoutes.of[Token, IO] {
    case req@POST -> Root / "order" / "add" as _ => for {
      order <- req.req.as[Order]
      uuid <- UUIDGen.randomString[IO]
      _ <- IO.fromFuture(IO.delay(saveOrder(Order(uuid, order.typeOfWork, order.date, order.announcedDateOfWork, order.factDateOfWork,
        order.numberOfSeal, order.natureOfCargo, order.weight, order.containerType, order.numberContainer,
        order.sizeContainer, order.specialMarks, order.companyId))))
      resp <- Ok("ok")
    } yield resp
  }
}
