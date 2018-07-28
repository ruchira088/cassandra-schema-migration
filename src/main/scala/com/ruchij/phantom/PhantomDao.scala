package com.ruchij.phantom

import com.outworkers.phantom.Table
import scalaz.OptionT

import scala.concurrent.{ExecutionContext, Future}

trait PhantomDao[A, B <: Table[B, A]]
{
  def createTable()(implicit executionContext: ExecutionContext): OptionT[Future, B]
}