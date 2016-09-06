package com.learning.akka.hello

/**
 * Created by lgrcyanny on 15/10/7.
 */
object Main {
  def main(args: Array[String]) {
    akka.Main.main(Array(classOf[HelloWorld].getName))
  }
}
