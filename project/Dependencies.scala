import sbt._

object Dependencies
{
  lazy val phantomDsl = "com.outworkers" %% "phantom-dsl" % "2.24.10"

  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.25"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10"

  lazy val scalaReflect = "org.scala-lang" % "scala-reflect" % "2.12.6"

  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.3"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

  lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  val DOCKER_TEST_KIT_VERSION = "0.9.7"
  lazy val dockerTestKitScalaTest = "com.whisk" %% "docker-testkit-scalatest" % DOCKER_TEST_KIT_VERSION
  lazy val dockerTestKitSpotify = "com.whisk" %% "docker-testkit-impl-spotify" % DOCKER_TEST_KIT_VERSION
  lazy val dockerTestKitConfig = "com.whisk" %% "docker-testkit-config" % DOCKER_TEST_KIT_VERSION

  lazy val faker = "com.github.javafaker" % "javafaker" % "0.15"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
