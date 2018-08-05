package com.ruchij.docker

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoint, ContactPoints}
import com.ruchij.utils.config.CassandraConfiguration
import com.whisk.docker.DockerContainer
import com.whisk.docker.config.DockerKitConfig
import com.whisk.docker.impl.spotify.DockerKitSpotify
import com.whisk.docker.scalatest.DockerTestKit
import org.scalatest.AsyncTestSuite

import scala.concurrent.duration._
import scala.language.postfixOps

trait DockerCassandra extends DockerTestKit with DockerKitSpotify with DockerKitConfig
{
  self: AsyncTestSuite =>

  override val StartContainersTimeout: FiniteDuration = 2 minutes

  val cassandra: DockerContainer = configureDockerContainer("docker.cassandra")

  def cassandraConnection(): CassandraConnection = ContactPoint.local.keySpace(CassandraConfiguration.DEFAULT_KEYSPACE)

  override def dockerContainers: List[DockerContainer] = cassandra :: super.dockerContainers
}