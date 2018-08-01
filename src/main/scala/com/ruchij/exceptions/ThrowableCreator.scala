package com.ruchij.exceptions

trait ThrowableCreator[-T]
{
  def throwable(value: T): Throwable
}

object ThrowableCreator
{
  implicit val exceptionThrowableCreator: ThrowableCreator[Exception] = identity[Exception]
}