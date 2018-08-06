package com.ruchij.lock

import com.ruchij.RandomGenerator
import com.ruchij.docker.DockerCassandra
import org.scalatest.{AsyncFlatSpec, MustMatchers}

import scala.concurrent.ExecutionContext

class PhantomLockServiceSpec extends AsyncFlatSpec with DockerCassandra with MustMatchers
{
  "phantomLockService.acquireLock" should "create a row in the lock table" in {

    val phantomLockService = PhantomLockService(cassandraConnection())

    val randomKeyspace = RandomGenerator.keySpace()

    for {
      _ <- phantomLockService.init()
      lock <- phantomLockService.acquireLock(randomKeyspace)
      _ = println(lock)
//      _ <- phantomLockService.acquireLock(randomKeyspace)
    }
    yield 1 mustBe 1
  }

  override implicit def executionContext: ExecutionContext = dockerExecutionContext
}
