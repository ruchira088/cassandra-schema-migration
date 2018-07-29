package com.ruchij.migration.phantom

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import com.ruchij.migration.{Migration, MigrationDao}
import com.ruchij.phantom.PhantomDao
import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

class PhantomMigrationDao(cassandraConnection: CassandraConnection)
  extends Database[PhantomMigrationDao](cassandraConnection) with MigrationDao with PhantomDao[Migration, PhantomMigrationTable]
{
  override type InitializationResult = Option[PhantomMigrationTable]

  object migrationTable extends PhantomMigrationTable with Connector

  override def insert(migration: Migration)(implicit executionContext: ExecutionContext): Future[Migration] =
    for {
      resultsSet <- migrationTable.store(migration).future()
    }
    yield migration

  override def getByVersionNumber(versionNumber: Int)(implicit executionContext: ExecutionContext): OptionT[Future, Migration] =
    OptionT[Future, Migration] {
      for {
        results <- migrationTable.select.where(_.versionNumber is versionNumber).fetch()
      }
      yield results.headOption
    }

  migrationTable.create.ifNotExists().table

  override def getAll()(implicit executionContext: ExecutionContext): Future[List[Migration]] =
    migrationTable.select.fetch()

  override def init()(implicit executionContext: ExecutionContext): Future[Option[PhantomMigrationTable]] =
    createTable().run

  override def createTable()(implicit executionContext: ExecutionContext): OptionT[Future, PhantomMigrationTable] =
    OptionT {
      for {
        resultsSet <- migrationTable.create.ifNotExists().future()
        result = if (resultsSet.forall(_.wasApplied())) Some(migrationTable) else None
      }
      yield result
    }
}

object PhantomMigrationDao
{
  def apply(cassandraConnection: CassandraConnection): PhantomMigrationDao =
    new PhantomMigrationDao(cassandraConnection)
}
