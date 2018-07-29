package com.ruchij.migration
import com.datastax.driver.core.{ResultSet, Session}
import com.ruchij.cql.CqlParser.CqlStatement
import com.ruchij.cql.CqlScript
import com.ruchij.exceptions.AlreadyAppliedCqlScriptException
import com.ruchij.lock.LockService
import com.ruchij.utils.ScalaUtils.fromEither

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.Future.fromTry
import scala.util.Try
import scalaz.std.scalaFuture.futureInstance

class MigrationServiceImpl(migrationDao: MigrationDao, session: Session, lockService: LockService) extends MigrationService
{
  override def applyLatest()(implicit executionContext: ExecutionContext): Unit = ???

  override def dryRun()(implicit executionContext: ExecutionContext): Unit = ???

  def apply(cqlScripts: List[CqlScript])(implicit executionContext: ExecutionContext): Future[List[ResultSet]] =
    cqlScripts.headOption
      .fold[Future[List[ResultSet]]](Future.successful(List.empty)) {
        cqlScript =>
          for {
            migration <- fromTry(MigrationService.migrationFromCqlScript(cqlScript))

            _ <- migrationDao.getByVersionNumber(migration.versionNumber)
                  .flatMapF(appliedMigration => fromEither(Migration.isMatch(appliedMigration, migration)))
                  .flatMapF[Migration](_ => Future.failed(AlreadyAppliedCqlScriptException(cqlScript)))
                  .run

            resultSets <- Future.sequence(migration.cqlStatements.map(executeCql))

            _ <- migrationDao.insert(migration)

            rest <- apply(cqlScripts.tail)
          }
          yield resultSets ++ rest
      }

  override def apply(keySpaceLock: String, cqlScripts: CqlScript*)(implicit executionContext: ExecutionContext): Future[List[ResultSet]] =
    for {
      lock <- lockService.acquireLock(keySpaceLock)

      resultSets <- apply(cqlScripts.toList)

      _ <- lockService.releaseLock(lock)
    }
    yield resultSets

  override def executeCql(cql: CqlStatement)(implicit executionContext: ExecutionContext): Future[ResultSet] =
  {
    println(cql)
    val promise = Promise[ResultSet]

    val result = session.executeAsync(cql)

    result.addListener(
      () => Try(result.get()).fold(promise.failure, promise.success),
      runnable => executionContext.execute(runnable)
    )

    promise.future
  }
}

object MigrationServiceImpl
{
  def apply(migrationDao: MigrationDao, session: Session, lockService: LockService): MigrationServiceImpl =
    new MigrationServiceImpl(migrationDao, session, lockService)
}
