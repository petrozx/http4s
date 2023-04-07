package config.util

import cats.effect.IO
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, ProducerSettings}

object Kafka {

  val consumerSettings: ConsumerSettings[IO, String, String] =
    ConsumerSettings[IO, String, String]
      .withBootstrapServers("192.168.0.13:9092")
      .withGroupId("crm-group")
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withEnableAutoCommit(true)

  val producerSettings: ProducerSettings[IO, String, String] =
    ProducerSettings[IO, String, String]
      .withBootstrapServers("192.168.0.13:9092")

}
