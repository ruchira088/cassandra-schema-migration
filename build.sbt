import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.ruchij",
      scalaVersion := "2.12.6"
    )),
    name := "cassandra-schema-migration",
    libraryDependencies ++= Seq(
      cassandraDriverCore, quillCassandra, phantomDsl,
      jodaTime, scalazCore,
      scalaTest % Test,
      pegdown % Test
    ),
    buildInfoKeys := BuildInfoKey.ofN(name, scalaVersion, sbtVersion),
    buildInfoPackage := "com.eed3si9n.ruchij",
    assemblyJarName in assembly := "cassandra-schema-migration-assembly.jar"
  )

enablePlugins(BuildInfoPlugin)

testOptions in Test +=
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")

addCommandAlias("testWithCoverage", "; clean; coverage; test; coverageReport")
