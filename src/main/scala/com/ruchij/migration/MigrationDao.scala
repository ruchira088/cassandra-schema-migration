package com.ruchij.migration

import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

trait MigrationDao
{
  def insert(migration: Migration)(implicit executionContext: ExecutionContext): Future[Migration]

  def getByVersionNumber(versionNumber: Int)(implicit executionContext: ExecutionContext): OptionT[Future, Migration]

  def getAll()(implicit executionContext: ExecutionContext): Future[List[Migration]]
}
