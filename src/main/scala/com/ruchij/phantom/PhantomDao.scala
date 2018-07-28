package com.ruchij.phantom

import com.outworkers.phantom.Table

import scala.concurrent.{ExecutionContext, Future}

trait PhantomDao[A, B <: Table[B, A]]
{
  def createTable()(implicit executionContext: ExecutionContext): Future[B]
}