package transactional

import cats.effect.IO

import scala.concurrent.Future

object Transaction {

  def transaction[A](a : Future[A]): IO[A] = {
    IO.fromFuture(IO.delay(a))
  }
}
