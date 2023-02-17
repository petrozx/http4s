import cats.effect.{ExitCode, IO, IOApp}
import config.util.ServerUtils.{host, port}
import org.http4s.dsl.io._
import org.http4s.ember.server.EmberServerBuilder
import routes.httpApp

object Main extends IOApp{

  override def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(host)
      .withPort(port)
      .withHttpApp(httpApp)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
