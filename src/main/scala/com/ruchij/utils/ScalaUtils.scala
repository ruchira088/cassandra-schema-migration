package com.ruchij.utils

import com.ruchij.exceptions.ThrowableCreator

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def predicate(condition: Boolean, throwable: => Throwable): Future[Unit] =
    Future.fromTry(tryPredicate(condition, throwable))

  def tryPredicate(condition: Boolean, throwable: => Throwable): Try[Unit] =
    if (condition) Success((): Unit) else Failure(throwable)

  def fromOption[A](option: Option[A], throwable: => Throwable): Future[A] =
    option.fold[Future[A]](Future.failed(throwable))(Future.successful)

  def fromEither[A, B](either: Either[A, B])(implicit throwableCreator: ThrowableCreator[A]): Future[B] =
    either.fold[Future[B]](a => Future.failed(throwableCreator.throwable(a)), Future.successful)

  def trySequence[A](list: List[Try[A]]): Either[List[Throwable], List[A]] =
    list match {
      case Nil => Right(List.empty[A])
      case Success(value) :: tail => trySequence(tail).map(value :: _)
      case Failure(throwable) :: tail => Left(throwable :: failureSequence(tail))
    }

  def failureSequence[A](list: List[Try[A]]): List[Throwable] =
    list match {
      case Failure(throwable) :: tail => throwable :: failureSequence(tail)
      case _ :: tail => failureSequence(tail)
      case Nil => List.empty
    }
}