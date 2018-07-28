import sbt._

object Dependencies
{
  lazy val quillCassandra = "io.getquill" %% "quill-cassandra" % "2.5.4"

  lazy val cassandraDriverCore = "com.datastax.cassandra" % "cassandra-driver-core" % "3.5.1"

  lazy val phantomDsl = "com.outworkers" %% "phantom-dsl" % "2.24.10"

  lazy val scalazCore = "org.scalaz" %% "scalaz-core" % "7.2.25"

  lazy val jodaTime = "joda-time" % "joda-time" % "2.10"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"

  lazy val pegdown = "org.pegdown" % "pegdown" % "1.6.0"
}
