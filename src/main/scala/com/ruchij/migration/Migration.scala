package com.ruchij.migration

import org.joda.time.DateTime

case class Migration(
    versionNumber: Int,
    name: String,
//    updatedAt: DateTime,
    checkSum: String,
//    cqlScript: List[String]
)