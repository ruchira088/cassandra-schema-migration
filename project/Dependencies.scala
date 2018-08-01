import sbt._

object Dependencies
{
  lazy val phantomDsl = "com.outworkers" %% "phantom-dsl" % "2.24.10"

  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.25"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10"

  lazy val scalaReflect = "org.scala-lang" % "scala-reflect" % "2.12.6"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  val DOCKER_TEST_KIT_VERSION = "0.9.5"
  lazy val dockerTestKitScalaTest = "com.whisk" %% "docker-testkit-scalatest" % DOCKER_TEST_KIT_VERSION
  lazy val dockerTestKitSpotify = "com.whisk" %% "docker-testkit-impl-spotify" % DOCKER_TEST_KIT_VERSION
  lazy val dockerTestKitConfig = "com.whisk" %% "docker-testkit-config" % DOCKER_TEST_KIT_VERSION

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
