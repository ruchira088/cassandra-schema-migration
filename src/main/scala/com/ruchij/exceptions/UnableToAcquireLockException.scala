package com.ruchij.exceptions

case class UnableToAcquireLockException(keySpaceName: String) extends Exception
