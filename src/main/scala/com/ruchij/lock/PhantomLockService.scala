package com.ruchij.lock

import java.util.UUID

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import com.ruchij.phantom.PhantomDao
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

class PhantomLockService(cassandraConnection: CassandraConnection)
  extends Database[PhantomLockService](cassandraConnection) with LockService with PhantomDao[Lock, PhantomLockTable]
{
  object phantomLockTable extends PhantomLockTable with Connector

  override def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      lock <- Future.successful(Lock(ownerId, DateTime.now(), keySpaceName))
      result <- phantomLockTable.store(lock).ifNotExists().future()
    }
    yield lock

  override def releaseLock(keySpaceName: String, ownerId: UUID)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      lock <- phantomLockTable.select.where(_.keySpace is keySpaceName).fetch()
      result <- phantomLockTable.delete.where(_.keySpace is keySpaceName).and(_.ownerId is ownerId).future()
    }
    yield lock.head

  override def createTable()(implicit executionContext: ExecutionContext): Future[PhantomLockTable] =
    for {
      resultSet <- phantomLockTable.create.ifNotExists().future()
    }
    yield phantomLockTable
}
