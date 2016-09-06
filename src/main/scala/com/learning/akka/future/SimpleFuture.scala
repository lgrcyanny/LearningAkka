package com.learning.akka.future

import akka.actor.{Props, ActorSystem, Actor}
import akka.util.Timeout

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration._

import akka.pattern._

class FutureHandlerActor extends Actor {
  def receive = {
    case "hello" => sender() ! "hi"
    case msg => println(s"Processing: $msg")
  }
}


object SimpleFuture {
  def main(args: Array[String]) {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val timeout = Timeout(5 seconds)
    val system = ActorSystem("FutureTest")
    val actor = system.actorOf(Props[FutureHandlerActor], "FutureHandler")
    val future = (actor ? "hello").mapTo[String]
    future pipeTo actor

    // use future directly
    val promise = Promise[String]()
    val promiseFuture = promise.future
    promise.success("promise success")
    promiseFuture foreach println

    // combine futures
    val calculateFuture = for {
      a <- Future(10 / 2)
      b <- Future(a + 1)
      c <- Future(a - 1)
      if c > 3
    } yield b * c
    calculateFuture foreach println
  }
}
