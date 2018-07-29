package com.ruchij.migration

import com.ruchij.cql.CqlParser.CqlStatement
import com.ruchij.utils.ScalaUtils.{tryPredicate, trySequence}
import org.joda.time.DateTime

import scala.util.Try

case class Migration(
    versionNumber: Int,
    name: String,
    updatedAt: DateTime,
    checkSum: String,
    cqlStatements: List[CqlStatement],
    cqlScript: List[String]
)

object Migration
{
  def isMatch(migrationOne: Migration, migrationTwo: Migration): Either[List[Throwable], _] =
    trySequence {
      List(
        tryPredicate(migrationOne.versionNumber == migrationTwo.versionNumber, ???),
        tryPredicate(migrationOne.name == migrationTwo.name, ???),
        tryPredicate(migrationOne.checkSum == migrationTwo.checkSum, ???)
      )
    }
}