package com.learning.akka.actors

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.event.Logging
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

/**
 * Created by lgrcyanny on 15/10/10.
 */
class MyActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "test" => log.info("receive test")
    case _ =>
      log.info("receive unknown message")
      self ! PoisonPill
  }

  override def postStop: Unit = {
    println("Stopping MyActor")
  }
}

object MyActor {
  def main(args: Array[String]) {
    val system = ActorSystem("MyActor")
    val myactor = system.actorOf(Props[MyActor])
    val duration = FiniteDuration(5, TimeUnit.SECONDS)
    implicit val timeout = Timeout(duration)
    myactor ! "hello"
  }
}
