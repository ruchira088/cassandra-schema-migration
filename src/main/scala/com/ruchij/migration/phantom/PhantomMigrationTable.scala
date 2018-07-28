package com.ruchij.migration.phantom

import com.outworkers.phantom.Table
import com.outworkers.phantom.keys.PartitionKey
import com.ruchij.migration.Migration

trait PhantomMigrationTable extends Table[PhantomMigrationTable, Migration]
{
  object versionNumber extends IntColumn with PartitionKey

  object name extends StringColumn

  object updatedAt extends DateTimeColumn

  object checkSum extends StringColumn

  object cqlScript extends ListColumn[String]
}
