package com.ruchij.exceptions

import com.ruchij.lock.Lock

case class UnableToReleaseLockException(lock: Lock) extends Exception
