package com.ruchij.utils.config

case class EnvironmentVariables(values: Map[String, String])

object EnvironmentVariables
{
  def getEnvValue(key: String)(implicit environmentVariables: EnvironmentVariables): Option[String] =
    environmentVariables.values.get(key)
}