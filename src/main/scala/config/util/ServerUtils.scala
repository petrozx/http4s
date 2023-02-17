package config.util

import com.comcast.ip4s.IpLiteralSyntax
import org.http4s.Uri
import org.http4s.Uri.Scheme.http
import org.http4s.headers.Origin.Host

object ServerUtils {
  val host = host"localhost"
  val port = port"8080"

  val secureHost = Host(http, Uri.Host.unsafeFromString("localhost"), Some(3000))
}
