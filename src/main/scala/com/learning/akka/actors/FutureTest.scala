package com.learning.akka.actors

import java.util.concurrent.Executors

import scala.concurrent.{Future, Promise, ExecutionContext}

/**
 * Created by lgrcyanny on 15/10/20.
 */
object FutureTest {
  def main(args: Array[String]) {
    implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(10))
    val f: Future[String] = Future {
      "hello"
    }.mapTo[String]
  }
}
