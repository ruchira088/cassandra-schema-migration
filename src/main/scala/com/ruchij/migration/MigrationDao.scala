package com.ruchij.migration

import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

trait MigrationDao
{
  type InitializationResult

  def insert(migration: Migration)(implicit executionContext: ExecutionContext): Future[Migration]

  def getByVersionNumber(versionNumber: Int)(implicit executionContext: ExecutionContext): OptionT[Future, Migration]

  def getAll()(implicit executionContext: ExecutionContext): Future[List[Migration]]

  def init()(implicit executionContext: ExecutionContext): Future[InitializationResult]
}
