package com.learning.akka.future

import scala.concurrent.Future

/**
 * Created by lgrcyanny on 15/12/29.
 */
object FutureWithList {
  def main(args: Array[String]) {
    import scala.concurrent.ExecutionContext.Implicits.global
    val listOfFutures: List[Future[Int]] = List.fill(100)(Future(1).mapTo[Int])
    val futureList: Future[List[Int]] = Future.sequence(listOfFutures)
    val sum = futureList.map(_.sum)
    sum foreach println

    // sum with fold
    val futures = for (i <- 1 to 1000) yield Future(i * 2)
    val futureSum = Future.fold(futures)(0)(_ + _)
    futureSum foreach println
  }
}
