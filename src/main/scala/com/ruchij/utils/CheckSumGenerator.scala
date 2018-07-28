package com.ruchij.utils

import java.security.MessageDigest

import scala.util.Try

object CheckSumGenerator
{
  sealed trait HashAlgorithm
  {
    def key: String

    def digest(): Try[MessageDigest] = Try(MessageDigest.getInstance(key))
  }

  case object Md5 extends HashAlgorithm { override def key: String = "MD5" }
  case object Sha extends HashAlgorithm { override def key: String = "SHA" }

  def generate(string: String, hashAlgorithm: HashAlgorithm): Try[String] =
    hashAlgorithm.digest()
      .map(_.digest(string.getBytes))
      .map(_.map("%02X".format(_)).mkString)
}
