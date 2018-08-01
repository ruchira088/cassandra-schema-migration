package com.ruchij.migration

import com.datastax.driver.core.ResultSet
import com.ruchij.cql.CqlParser.CqlStatement
import com.ruchij.cql.CqlScript
import com.ruchij.utils.CheckSumGenerator
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

trait MigrationService
{
  def applyLatest()(implicit executionContext: ExecutionContext)

  def dryRun()(implicit executionContext: ExecutionContext)

  def apply(keyspaceLock: String, cqlScripts: CqlScript*)(implicit executionContext: ExecutionContext): Future[List[ResultSet]]

  def executeCql(cql: CqlStatement)(implicit executionContext: ExecutionContext): Future[ResultSet]
}

object MigrationService
{
  def migrationFromCqlScript(cqlScript: CqlScript): Try[Migration] =
    for {
      checkSum <- CheckSumGenerator.generate(cqlScript.cqlStatements.mkString, CheckSumGenerator.Md5)
    }
    yield Migration(cqlScript.versionNumber, cqlScript.name, DateTime.now(), checkSum, cqlScript.cqlStatements, cqlScript.contents)
}