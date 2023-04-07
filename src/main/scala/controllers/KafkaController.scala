package controllers
import cats.effect._
import config.util.Kafka._
import config.util.{Topics}
import fs2.{Chunk, Stream}
import fs2.kafka.{KafkaConsumer, KafkaProducer, ProducerRecord, ProducerRecords}
import org.http4s._
import org.http4s.dsl.Http4sDsl

import scala.concurrent.duration.DurationInt



object KafkaController {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

  def consumeMessages =
    KafkaConsumer.stream(consumerSettings)
      .evalTap(_.subscribeTo(Topics.ticket_status_updates.toString))
      .flatMap(_.stream)
      .map(_.record.value)

    def produceMessages(value: String): IO[Unit] =
      Stream(ProducerRecords.one(
        ProducerRecord[String, String](Topics.ticket_status_updates.toString, "#", value)
      ))
        .covary[IO]
        .through(KafkaProducer.pipe(producerSettings))
        .compile
        .drain

    def kafkaRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "messages" =>
        val responseBody: Stream[IO, Byte] =
          consumeMessages
            .map(msg => Chunk.array(msg.getBytes))
            .interruptAfter(1.seconds)
            .flatMap(Stream.chunk[IO, Byte])
        Ok(responseBody)

      case req@POST -> Root / "messages" =>
        for {
          value <- req.as[String]
          _ <- IO.println(value)
          _ <- IO(println(s"Received POST request with value $value"))
          _ <- produceMessages(value)
          resp <- Created()
        } yield resp
    }
}
