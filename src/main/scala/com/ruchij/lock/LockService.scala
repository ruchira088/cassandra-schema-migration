package com.ruchij.lock

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

trait LockService
{
  def serviceId: UUID = LockService.SERVICE_ID

  def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock]

  def releaseLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock]
}

object LockService
{
  val SERVICE_ID: UUID = UUID.randomUUID()
}