package com.ruchij

import org.scalatest.{FlatSpec, MustMatchers}
import TestImplicits.StringWrapper

class CqlParserSpec extends FlatSpec with MustMatchers
{
  "CqlParser.removeMultiLineComments(List[String])" should "return the list of strings with comments removed" in {

    import com.ruchij.cql.CqlParser.removeMultiLineComments

    val inputWithComments =
      s"""
         |Hello World/* How
         | are */ are/*
         | foo
         | bar */ hello
         |you /
         |
         |How /* hello */ you
         |Foo"""
        .stripMargin

    val expectedOutputWithComments =
      """
        |Hello World
        | are
        |
        | hello
        |you /
        |
        |How  you
        |Foo"""
        .stripMargin

    removeMultiLineComments(inputWithComments.splitIntoLines) mustBe Some(expectedOutputWithComments.splitIntoLines)

    val textWithoutComments =
      """
        |Hello World,
        |FooBar
        |How are you ?
      """.stripMargin

    removeMultiLineComments(textWithoutComments.splitIntoLines) mustBe Some(textWithoutComments.splitIntoLines)
    removeMultiLineComments(List("Hello World")) mustBe Some(List("Hello World"))
  }

  "CqlParser.removeSingleLineComment(String)" should "return the string after the single line comment is removed" in {

    import com.ruchij.cql.CqlParser.removeSingleLineComment

    removeSingleLineComment("Hello // World") mustBe "Hello "
    removeSingleLineComment("") mustBe ""
  }

  "CqlParser.parse(input: List[String])" should "return a list of parsed strings" in {

    import com.ruchij.cql.CqlParser.parse

    val input =
      """
        |Hello /* World
        |// How */
        |John;
        |// Smith
        |Foo Bar;
        |Hello;
        |How
        | Are
        | You ?
      """.stripMargin.splitIntoLines

    val expected = List("Hello John;", "Foo Bar;" , "Hello;", "How Are You ?;")

    println(parse(input))

    parse(input) mustBe Some(expected)
  }
}
