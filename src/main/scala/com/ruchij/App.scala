package com.ruchij

import com.outworkers.phantom.connectors.ContactPoints
import com.ruchij.lock.PhantomLockService
import com.ruchij.migration.phantom.PhantomMigrationDao

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object App
{
  def main(args: Array[String]): Unit =
  {
    val cassandraConnection = ContactPoints(List("localhost")).keySpace("migrations")

    val phantomMigrationDao = PhantomMigrationDao.reader(cassandraConnection)
    val lockingService = PhantomLockService.reader(cassandraConnection)

    val result =
      for {
        _ <- lockingService.createTable()
        lock <- lockingService.acquireLock("hello")
      }
      yield lock

    result.onComplete(println)

    Await.ready(result, 30 seconds)
  }
}
