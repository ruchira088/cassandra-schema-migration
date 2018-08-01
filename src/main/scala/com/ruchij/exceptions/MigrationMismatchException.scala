package com.ruchij.exceptions

case class MigrationMismatchException(throwables: List[Throwable]) extends Exception
