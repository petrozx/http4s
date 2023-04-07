package controllers

import cats.effect.IO
import cats.effect.std.UUIDGen
import config.util.Token
import database_service.entity.Order
import database_service.repositories.ChangesRepository.{allVisible, getChangesByOrders}
import database_service.repositories.OrderRepository.{saveOrder, takeOrdersByTokens, updateNewOrders}
import org.http4s.AuthedRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._

object OrderController {

  val orderController = AuthedRoutes.of[Token, IO] {
    case req@POST -> Root / "order" / "add" as _ => for {
      order <- req.req.as[Order]
      uuid <- UUIDGen.randomString[IO]
      _ <- IO.fromFuture(IO.delay(saveOrder(order.copy(orderID=uuid))))
      resp <- Ok("ok")
    } yield resp

    case GET -> Root / "orders" as token => for {
      orders1 <- IO.fromFuture(IO(takeOrdersByTokens(token)))
      orders2 <- IO(orders1.map {
        case (order, typeOfW) => (order.copy(typeOfWork = typeOfW.name))
      })
      orders3 <- IO.fromFuture(IO(getChangesByOrders(orders2)))
      resp <- Ok((orders2, orders3))
    } yield resp

    case GET -> Root / "seen" as token => for {
      res <- IO.fromFuture(IO(allVisible(token.userId)))
      _ <- if (token.role=="admin") IO.fromFuture(IO(updateNewOrders(token))) else IO.pure()
      resp <- Ok(res)
    } yield resp
  }
}
