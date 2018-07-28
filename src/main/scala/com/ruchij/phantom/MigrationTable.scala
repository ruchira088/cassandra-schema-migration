package com.ruchij.phantom

import com.outworkers.phantom.Table
import com.outworkers.phantom.keys.PartitionKey
import com.ruchij.migration.Migration

trait MigrationTable extends Table[MigrationTable, Migration]
{
  object versionNumber extends IntColumn with PartitionKey

  object name extends StringColumn

  object checkSum extends StringColumn
}
