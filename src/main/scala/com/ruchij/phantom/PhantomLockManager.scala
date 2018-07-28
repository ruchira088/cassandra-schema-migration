package com.ruchij.phantom

import java.util.UUID

import com.datastax.driver.core.{ResultSet, ResultSetFuture}
import com.outworkers.phantom.connectors.{CassandraConnection, KeySpace}
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import scalaz.Reader

import scala.concurrent.{ExecutionContext, Future, Promise}

object PhantomLockManager
{
  val LOCK_MANAGER_ID: String = UUID.randomUUID().toString

  val MIGRATION_LOCKS_TABLE_NAME = "migration_locks"

  def executeQuery(queryMapper: KeySpace => String)(implicit executionContext: ExecutionContext): Reader[CassandraConnection, Future[ResultSet]] =
    Reader {
      cassandraConnection =>

        val promise = Promise[ResultSet]

        val result: ResultSetFuture =
          cassandraConnection.session.executeAsync(queryMapper(cassandraConnection.provider.space))

        result.addListener(
          () => promise.success(result.get()),
          runnable => executionContext.execute(runnable)
        )

        promise.future
    }

  def executeQuery(query: String)(implicit executionContext: ExecutionContext): Reader[CassandraConnection, Future[ResultSet]] =
    executeQuery(_ => query)

  def createLockTable()(implicit executionContext: ExecutionContext): Reader[CassandraConnection, Future[ResultSet]] =
    executeQuery {
      keySpace =>
        s"""
           | CREATE TABLE IF NOT EXISTS ${keySpace.name}.$MIGRATION_LOCKS_TABLE_NAME(
           |   lock_manager_id UUID,
           |   created_at TIMESTAMP,
           |   key_space TEXT,
           |   PRIMARY KEY (key_space)
           | );
        """.stripMargin
    }

  def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Reader[CassandraConnection, Future[ResultSet]] =
    executeQuery {
      keySpace =>
        s"""
          | INSERT INTO ${keySpace.name}.$MIGRATION_LOCKS_TABLE_NAME(
          |   lock_manager_id, created_at, key_space
          | ) VALUES (
          |   $LOCK_MANAGER_ID,
          |   '${ISODateTimeFormat.dateTime().print(DateTime.now())}',
          |   '$keySpaceName'
          | ) IF NOT EXISTS;
        """.stripMargin
    }

  def releaseLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Reader[CassandraConnection, Future[ResultSet]] =
    executeQuery {
      keySpace =>
        s"""
           | DELETE FROM ${keySpace.name}.$MIGRATION_LOCKS_TABLE_NAME WHERE key_space = '$keySpaceName';
         """.stripMargin
    }
}
