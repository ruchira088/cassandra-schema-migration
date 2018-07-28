package com.ruchij

object TestImplicits
{
  implicit class StringWrapper(string: String)
  {
    def splitIntoLines: List[String] = string.split("\n").toList
  }
}
