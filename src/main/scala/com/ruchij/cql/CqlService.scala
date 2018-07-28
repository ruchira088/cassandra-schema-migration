package com.ruchij.cql

import java.io.File
import java.nio.file.{Path, Paths}

import com.ruchij.file.FileReader
import com.ruchij.utils.ScalaUtils.fromOption

import scala.concurrent.{ExecutionContext, Future}

object CqlService
{
  val CQL_FOLDER: Path = Paths.get("cql_scripts")

  def allCqlScripts(): List[File] =
    CQL_FOLDER.toFile.listFiles().toList

  def readCqlScript(path: Path)(implicit executionContext: ExecutionContext): Future[CqlScript] =
    for {
      cqlText <- FileReader.readTextFile(path)
      (versionNumber, name) <- fromOption(CqlScript.info(path), ???)
    }
    yield CqlScript(versionNumber, name, cqlText)

//  def readAllCqlScripts()(implicit executionContext: ExecutionContext): Future[List[CqlScript]] =

}
