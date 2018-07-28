package com.ruchij

import com.datastax.driver.core.Cluster
import com.ruchij.quill.QuillManager
import io.getquill._
import io.getquill.context.cassandra.cluster.ClusterBuilder

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object App
{
  def main(args: Array[String]): Unit =
  {
    Cluster.builder()
      .addContactPoints("")
      .build()
      .connectAsync()
//    implicit val cassandraAsyncContext: CassandraAsyncContext[SnakeCase] =
//      new CassandraAsyncContext(SnakeCase, "cassandra")
//
//    println(Await.result(QuillManager.initialize(), 30 seconds))
  }
}
