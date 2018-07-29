package com.ruchij

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.ruchij.cql.CqlScript
import com.ruchij.lock.PhantomLockService
import com.ruchij.migration.phantom.PhantomMigrationDao
import com.ruchij.migration.{MigrationService, MigrationServiceImpl}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object App
{
  def main(args: Array[String]): Unit =
  {
    val cassandraConnection: CassandraConnection = ContactPoints(List("localhost")).keySpace("migrations")

    val migrationDao: PhantomMigrationDao = PhantomMigrationDao(cassandraConnection)
    val lockService: PhantomLockService = PhantomLockService(cassandraConnection)
    val migrationService: MigrationService = MigrationServiceImpl(migrationDao, cassandraConnection.session.init(), lockService)

    val result =
      for {
        _ <- migrationDao.init()
        _ <- lockService.init()

        cqlScripts <- CqlScript.allCqlScripts()
        results <- migrationService.apply("play_api_sparkle", cqlScripts.sortBy(_.versionNumber): _*)
      }
      yield results

    println(Await.result(result, 30 seconds))
  }
}
