package com.learning.akka.future

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by lgrcyanny on 16/1/3.
 */
object FutureTest {
  def main(args: Array[String]) {
    import scala.concurrent.ExecutionContext.Implicits.global
    val future = Future {
      "hello"
    }.mapTo[String]
    future.onSuccess{
      case "hello" => println("hello")
    }
  }
}
