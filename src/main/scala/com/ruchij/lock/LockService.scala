package com.ruchij.lock

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

trait LockService
{
  type InitializationResult

  def serviceId: UUID = LockService.SERVICE_ID

  def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock]

  def releaseLock(lock: Lock)(implicit executionContext: ExecutionContext): Future[Lock]

  def init()(implicit executionContext: ExecutionContext): Future[InitializationResult]
}

object LockService
{
  val SERVICE_ID: UUID = UUID.randomUUID()
}