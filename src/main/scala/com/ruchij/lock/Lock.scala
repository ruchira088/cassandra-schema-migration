package com.ruchij.lock

import java.util.UUID

import org.joda.time.DateTime

case class Lock(
    ownerId: UUID,
    lockedAt: DateTime,
    lockedKeySpace: String
)
