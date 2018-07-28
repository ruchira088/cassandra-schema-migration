package com.ruchij.migration

import com.ruchij.cql.{CqlParser, CqlScript}
import com.ruchij.utils.CheckSumGenerator
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext

trait MigrationService
{
  def applyLatest()(implicit executionContext: ExecutionContext)

  def dryRun()(implicit executionContext: ExecutionContext)
}

object MigrationService
{
  def migration(cqlScript: CqlScript): Option[Migration] =
    for {
      parsedContents <- CqlParser.removeComments(cqlScript.contents)
      checkSum <- CheckSumGenerator.generate(parsedContents.mkString, CheckSumGenerator.Md5).toOption
    }
    yield Migration(cqlScript.versionNumber, cqlScript.name, DateTime.now(), checkSum, parsedContents, cqlScript.contents)
}