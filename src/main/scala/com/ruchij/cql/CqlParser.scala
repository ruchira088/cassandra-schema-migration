package com.ruchij.cql

import com.ruchij.Constants

object CqlParser
{
  def removeMultiLineComments(input: List[String]): Option[List[String]] =
    removeMultiLineComments(input, List.empty, true)

  private def removeMultiLineComments(input: List[String], output: List[String], start: Boolean): Option[List[String]] =
    input match {
      case head :: tail =>
        head.toList match {
          case '/' :: '*' :: xs =>
            dropUntilEndOfComment(xs.mkString :: tail).flatMap(removeMultiLineComments(_, output, false))

          case x :: xs =>
            removeMultiLineComments(
              xs.mkString :: tail,
              if (start)
                output ++ List(x.toString)
              else
                output.init :+ (output.last :+ x),
              start = false
            )

          case _ =>
            if (start)
              removeMultiLineComments(tail, output ++ List(Constants.EMPTY_STRING), true)
            else
              removeMultiLineComments(tail, output, true)
        }

      case Nil => Some(output)
    }

  private def dropUntilEndOfComment(text: List[String]): Option[List[String]] =
    text.headOption
      .map(_.toList)
      .flatMap {
        case '*' :: '/' :: xs => Some(xs.mkString :: text.tail)
        case _ :: xs => dropUntilEndOfComment(xs.mkString :: text.tail)
        case _ => dropUntilEndOfComment(text.tail).map(Constants.EMPTY_STRING :: _)
      }

  def removeSingleLineComment(input: String): String =
    removeSingleLineComment(input, Constants.EMPTY_STRING)

  private def removeSingleLineComment(text: String, output: String): String =
    text.toList match {
      case '/' :: '/' :: _ => output
      case x :: xs => removeSingleLineComment(xs.mkString, output :+ x)
      case _ => output
    }
}