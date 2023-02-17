ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "http4s"
  )
scalacOptions ++= Seq(
  "-Ymacro-annotations"
)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.9.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.6"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.23.18",
  "org.http4s" %% "http4s-ember-server" % "0.23.18",
  "org.http4s" %% "http4s-ember-client" % "0.23.18",
  "org.http4s" %% "http4s-circe" % "0.23.18"
)
libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % "2.0.5",
  "org.slf4j" % "slf4j-simple" % "2.0.5"
)
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.5.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "com.typesafe.slick" %% "slick-codegen" % "3.4.1"
)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.4",
  "io.circe" %% "circe-generic" % "0.14.4",
  "io.circe" %% "circe-parser" % "0.14.4",
  "io.circe" %% "circe-generic-extras" % "0.14.3",
  "io.circe" %% "circe-literal" % "0.14.4",
  "com.github.jwt-scala" %% "jwt-circe" % "9.1.2"
)
libraryDependencies ++= Seq(
  "at.favre.lib" % "bcrypt" % "0.10.1",
  "com.github.jwt-scala" %% "jwt-circe" % "9.1.2"
)