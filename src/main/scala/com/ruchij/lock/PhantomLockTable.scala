package com.ruchij.lock

import com.outworkers.phantom.Table
import com.outworkers.phantom.keys.PartitionKey

trait PhantomLockTable extends Table[PhantomLockTable, Lock]
{
  object ownerId extends UUIDColumn

  object lockedAt extends DateTimeColumn

  object keySpace extends StringColumn with PartitionKey
}
