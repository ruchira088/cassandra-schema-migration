package com.ruchij.lock

import java.util.UUID

import scala.concurrent.{ExecutionContext, Future}

trait LockService
{
  def ownerId: UUID = LockService.LOCKER_OWNER_ID

  def acquireLock(keySpaceName: String)(implicit executionContext: ExecutionContext): Future[Lock]

  def releaseLock(keySpaceName: String, ownerId: UUID)(implicit executionContext: ExecutionContext): Future[Lock]
}

object LockService
{
  val LOCKER_OWNER_ID: UUID = UUID.randomUUID()
}