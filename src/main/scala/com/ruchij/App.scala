package com.ruchij

import com.outworkers.phantom.connectors.ContactPoints
import com.ruchij.migration.phantom.PhantomMigrationDao
import com.ruchij.phantom.PhantomLockManager

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object App
{
  def main(args: Array[String]): Unit =
  {
    val cassandraConnection = ContactPoints(List("localhost")).keySpace("migrations")
    val phantomMigrationDao = new PhantomMigrationDao(cassandraConnection)
    println(Await.result(phantomMigrationDao.createTable(), 30 seconds))

    println(Await.result(PhantomLockManager.createLockTable().run(cassandraConnection), 30 seconds))
    println(Await.result(PhantomLockManager.releaseLock("hello").run(cassandraConnection), 30 seconds))

  }
}
