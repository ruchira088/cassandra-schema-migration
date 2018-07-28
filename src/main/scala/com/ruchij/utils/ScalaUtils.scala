package com.ruchij.utils

import com.ruchij.exceptions.EmptyListException

import scala.concurrent.Future

object ScalaUtils
{
  def predicate(condition: Boolean, throwable: => Throwable): Future[Unit] =
    if (condition) Future.successful((): Unit) else Future.failed(throwable)

  def headFuture[A](list: List[A]): Future[A] =
    list.headOption.fold[Future[A]](Future.failed(EmptyListException))(Future.successful)

  def fromOption[A](option: Option[A], throwable: => Throwable): Future[A] =
    option.fold[Future[A]](Future.failed(throwable))(Future.successful)
}
