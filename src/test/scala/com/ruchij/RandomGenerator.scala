package com.ruchij

import com.github.javafaker.Faker

object RandomGenerator
{
  lazy val faker: Faker = Faker.instance()

  def keySpace(): String = s"${faker.name().username().replaceAll("""\.""", "_")}_keyspace"
}
