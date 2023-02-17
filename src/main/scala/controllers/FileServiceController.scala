package controllers

import cats.effect.IO
import cats.implicits._
import config.util.Token
import fs2.io.file.{Files, Path}
import org.http4s.AuthedRoutes
import org.http4s.dsl.io._
import org.http4s.multipart.Multipart

import scala.language.postfixOps

object FileServiceController {

  val saveMultipartFiles: AuthedRoutes[Token, IO] = AuthedRoutes.of {
    case req@POST -> Root / "files" as user =>
      req.req.decode[Multipart[IO]] {
        m => {
          for {
            _ <- IO.pure(m.parts.map(a => println(a.filename)))
            _ <- m.parts.parFoldMapA(p => writeToFile(p.body, p.filename.get))
            ok <- Ok("ok")
          } yield ok
        }
      }
  }

  private def writeToFile(stream: fs2.Stream[IO, Byte], fileName: String) = {
    val target = Path(fileName)
    stream.through(Files[IO].writeAll(target)).compile.drain.as(target)
  }
}
