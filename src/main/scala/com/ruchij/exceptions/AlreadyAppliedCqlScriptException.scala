package com.ruchij.exceptions

import com.ruchij.cql.CqlScript

case class AlreadyAppliedCqlScriptException(cqlScript: CqlScript) extends Exception