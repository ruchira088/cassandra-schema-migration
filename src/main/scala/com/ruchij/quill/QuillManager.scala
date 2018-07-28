package com.ruchij.quill

import io.getquill.{CassandraAsyncContext, SnakeCase}

import scala.concurrent.{ExecutionContext, Future}

object QuillManager
{
  def initialize()(implicit cassandraAsyncContext: CassandraAsyncContext[SnakeCase], executionContext: ExecutionContext): Future[Unit] =
    cassandraAsyncContext.executeAction {
      """
        | CREATE TABLE IF NOT EXISTS migration.migration_updates(
        |   version_number INT,
        |   name TEXT,
        |   updated_at TIMESTAMP,
        |   check_sum TEXT,
        |   cql_script TEXT,
        |   PRIMARY KEY (version_number)
        | );
      """.stripMargin
    }
}
