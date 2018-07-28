package com.ruchij.cql

import java.nio.file.Path

import scala.util.Try
import scala.util.matching.Regex

case class CqlScript(versionNumber: Int, name: String, contents: List[String])

object CqlScript
{
  val CqlFileNameRegex: Regex = """(\d+)_(\S+)\.cql""".r

  def info(path: Path): Option[(Int, String)] =
    path.toFile.getName match {
      case CqlFileNameRegex(versionNumber, name) => Try(versionNumber.toInt).toOption.map(_ -> name)
      case _ => None
    }
}