package com.ruchij.file

import java.nio.ByteBuffer
import java.nio.channels.{AsynchronousFileChannel, CompletionHandler}
import java.nio.file.{Path, StandardOpenOption}

import scala.concurrent.{ExecutionContext, Future, Promise}

object FileUtils
{
  def readFile(path: Path): Future[Array[Byte]] =
  {
    val promise = Promise[Array[Byte]]

    val fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)
    val byteBuffer = ByteBuffer.allocate(fileChannel.size().toInt)

    fileChannel.read(byteBuffer, 0, (): Unit, new CompletionHandler[Integer, Unit] {

      override def completed(v: Integer, unit: Unit): Unit =
        promise.success(byteBuffer.array())

      override def failed(throwable: Throwable, unit: Unit): Unit =
        promise.failure(throwable)
    })

    promise.future
  }

  def readTextFile(path: Path)(implicit executionContext: ExecutionContext): Future[List[String]] =
    readFile(path).map(new String(_).split("\n").toList)
}
