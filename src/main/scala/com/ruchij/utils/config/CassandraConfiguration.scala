package com.ruchij.utils.config

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

object CassandraConfiguration
{
  val DEFAULT_KEYSPACE = "migrations"

  private val typesafeConfig: Config = ConfigFactory.load()

  private def cassandraConfig(key: String): Try[String] =
    Try(typesafeConfig.getString(s"cassandra.$key"))

  def hosts(): List[String] =
    cassandraConfig("contactPoints").map(_.split(",").toList)
      .getOrElse(List("localhost"))

  def keySpace(): String =
    cassandraConfig("keySpace")
      .getOrElse(DEFAULT_KEYSPACE)

  def cassandraConnection(): CassandraConnection =
    ContactPoints(hosts()).keySpace(keySpace())
}