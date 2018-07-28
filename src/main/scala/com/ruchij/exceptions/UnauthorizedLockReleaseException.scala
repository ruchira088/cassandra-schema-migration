package com.ruchij.exceptions

import java.util.UUID

import com.ruchij.lock.Lock

case class UnauthorizedLockReleaseException(lock: Lock, serviceId: UUID) extends Exception
