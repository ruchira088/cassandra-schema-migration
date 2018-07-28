package com.ruchij.quill

import com.ruchij.migration.{Migration, MigrationDao}
import io.getquill.{CassandraAsyncContext, SnakeCase}
import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

class QuillMigrationDao(cassandraAsyncContext: CassandraAsyncContext[SnakeCase]) extends MigrationDao
{
  import cassandraAsyncContext._

  override def insert(migration: Migration)(implicit executionContext: ExecutionContext): Future[Migration] =
    for {
      result <- run { quote(query[Migration].insert(lift(migration))) }
    }
    yield migration

  override def getByVersionNumber(versionNumber: Int)(implicit executionContext: ExecutionContext): OptionT[Future, Migration] =
    OptionT[Future, Migration] {
      for {
        migrations <- run { quote(query[Migration].filter(_.versionNumber == lift(versionNumber))) }
      }
      yield migrations.headOption
    }

  override def getAll()(implicit executionContext: ExecutionContext): Future[List[Migration]] =
    run { quote(query[Migration]) }
}
