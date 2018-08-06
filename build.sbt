import Dependencies._

lazy val It = config("it") extend Test

lazy val root =
  (project in file("."))
    .enablePlugins(BuildInfoPlugin)
    .configs(It)
    .settings(
      inThisBuild(List(
        organization := "com.ruchij",
        scalaVersion := "2.12.6"
      )),
      name := "cassandra-schema-migration",
      libraryDependencies ++=
        List(
          scalaReflect,
          phantomDsl,
          jodaTime,
          scalazCore,
          typesafeConfig
        ) ++
          List(
            scalaTest,
            pegdown,
            dockerTestKitScalaTest, dockerTestKitSpotify, dockerTestKitConfig,
            faker
          ).map(_ % s"${Test.name}, ${It.name}"),
      buildInfoKeys := BuildInfoKey.ofN(name, scalaVersion, sbtVersion),
      buildInfoPackage := "com.eed3si9n.ruchij",
      assemblyJarName in assembly := "cassandra-schema-migration-assembly.jar",
      inConfig(It)(Defaults.itSettings),
      scalaSource in It := sourceDirectory.value / "it",
      fork in It := true,
      javaOptions in It += "-Dconfig.file=src/it/resources/application.it.conf",
      testOptions in Test +=
        Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-results")
    )

addCommandAlias("testWithCoverage", "; clean; coverage; test; it:test; coverageReport")
