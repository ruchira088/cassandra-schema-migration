package com.ruchij.cql

import java.nio.file.{Path, Paths}

import com.ruchij.cql.CqlParser.CqlStatement
import com.ruchij.file.FileReader
import com.ruchij.utils.ScalaUtils.fromOption

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.matching.Regex

case class CqlScript(versionNumber: Int, name: String, cqlStatements: List[CqlStatement], contents: List[String])

object CqlScript
{
  val CQL_FOLDER: Path = Paths.get("cql_scripts")

  val CqlFileNameRegex: Regex = """(\d+)_(\S+)\.cql""".r

  def info(path: Path): Option[(Int, String)] =
    path.toFile.getName match {
      case CqlFileNameRegex(versionNumber, name) => Try(versionNumber.toInt).toOption.map(_ -> name)
      case _ => None
    }

  def allCqlScripts()(implicit executionContext: ExecutionContext): Future[List[CqlScript]] =
    Future.sequence {
      CQL_FOLDER.toFile.listFiles().toList.map(file => readCqlScript(file.toPath))
    }

  def readCqlScript(path: Path)(implicit executionContext: ExecutionContext): Future[CqlScript] =
    for {
      cqlText <- FileReader.readTextFile(path)
      (versionNumber, name) <- fromOption(CqlScript.info(path), ???)
      parsedCql <- fromOption(CqlParser.parse(cqlText), ???)
    }
    yield CqlScript(versionNumber, name, parsedCql, cqlText)
}