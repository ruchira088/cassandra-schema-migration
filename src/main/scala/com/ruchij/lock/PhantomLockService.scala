package com.ruchij.lock

import java.util.UUID

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import com.ruchij.exceptions.{UnableToAcquireLockException, UnableToReleaseLockException, UnauthorizedLockReleaseException}
import com.ruchij.phantom.PhantomDao
import org.joda.time.DateTime
import scalaz.Reader
import com.ruchij.utils.ScalaUtils.{headFuture, predicate}

import scala.concurrent.{ExecutionContext, Future}

class PhantomLockService(cassandraConnection: CassandraConnection)
  extends Database[PhantomLockService](cassandraConnection) with LockService with PhantomDao[Lock, PhantomLockTable]
{
  object phantomLockTable extends PhantomLockTable with Connector

  override def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      lock <- Future.successful(Lock(serviceId, DateTime.now(), keySpaceName))
      result <- phantomLockTable.store(lock).ifNotExists().future()

      _ <- predicate(result.wasApplied(), UnableToAcquireLockException(keySpaceName))
    }
    yield lock

  override def releaseLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      locks <- phantomLockTable.select.where(_.lockedKeySpace is keySpaceName).fetch()
      lock <- headFuture(locks)
      _ <- predicate(lock.ownerId == serviceId, UnauthorizedLockReleaseException(lock, serviceId))

      result <- phantomLockTable.delete.where(_.lockedKeySpace is keySpaceName).future()
      _ <- predicate(result.wasApplied(), UnableToReleaseLockException(lock))

    }
    yield lock

  override def createTable()(implicit executionContext: ExecutionContext): Future[PhantomLockTable] =
    for {
      resultSet <- phantomLockTable.create.ifNotExists().future()
    }
    yield phantomLockTable
}

object PhantomLockService
{
  def reader: Reader[CassandraConnection, PhantomLockService] =
    Reader { new PhantomLockService(_) }
}
