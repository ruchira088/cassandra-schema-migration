package com.ruchij.lock

import com.outworkers.phantom.connectors.CassandraConnection
import com.outworkers.phantom.database.Database
import com.outworkers.phantom.dsl._
import com.ruchij.exceptions.{UnableToAcquireLockException, UnableToReleaseLockException, UnauthorizedLockReleaseException}
import com.ruchij.phantom.PhantomDao
import com.ruchij.utils.ScalaUtils.predicate
import org.joda.time.DateTime
import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

class PhantomLockService(cassandraConnection: CassandraConnection)
  extends Database[PhantomLockService](cassandraConnection) with LockService with PhantomDao[Lock, PhantomLockTable]
{
  override type InitializationResult = Option[PhantomLockTable]

  object phantomLockTable extends PhantomLockTable with Connector

  override def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      lock <- Future.successful(Lock(serviceId, DateTime.now(), keySpaceName))
      _ = println(lock)
      result <- phantomLockTable.store(lock).ifNotExists().future()

      _ <- predicate(result.wasApplied(), UnableToAcquireLockException(keySpaceName))
    }
    yield lock

  override def releaseLock(lock: Lock)(implicit executionContext: ExecutionContext): Future[Lock] =
    for {
      _ <- predicate(lock.ownerId == serviceId, UnauthorizedLockReleaseException(lock, serviceId))

      result <- phantomLockTable.delete.where(_.lockedKeySpace is lock.lockedKeySpace).future()
      _ <- predicate(result.wasApplied(), UnableToReleaseLockException(lock))
    }
    yield lock

  override def createTable()(implicit executionContext: ExecutionContext): OptionT[Future, PhantomLockTable] =
    OptionT {
      for {
        resultSet <- phantomLockTable.create.ifNotExists().future()
        result = if (resultSet.forall(_.wasApplied())) Some(phantomLockTable) else None
      }
      yield result
    }

  override def init()(implicit executionContext: ExecutionContext): Future[Option[PhantomLockTable]] =
    createTable().run
}

object PhantomLockService
{
  def apply(cassandraConnection: CassandraConnection): PhantomLockService =
    new PhantomLockService(cassandraConnection)
}
