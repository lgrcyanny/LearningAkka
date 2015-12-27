package com.learning.akka.actors

import akka.actor._
import akka.pattern._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
 * Created by lgrcyanny on 15/12/22.
 */
case class Hello(msg: String)
case object Shutdown
class GraceFullStopActor extends Actor {
  val worker = context.watch(context.actorOf(Props[GraceFullStopActor], "worker"))
  override def receive = {
    case Hello(msg) => println(Hello(msg))
    case Shutdown =>
      worker ! PoisonPill
      context become shuttingDown
  }
  def shuttingDown: Receive = {
    case Hello(msg) => println("service unavailable, shutting down")
    case Terminated(`worker`) =>
      println("Shutting down")
      context stop self
  }
}

object GraceFullStopActor {
  def main(args: Array[String]) {
    val system = ActorSystem("SimpleSystem")
    val simpleActor = system.actorOf(Props[GraceFullStopActor], "SimpleActor")
    println(simpleActor.path)
    simpleActor ! Hello("hello today")
    try {
      val stopped: Future[Boolean] = gracefulStop(simpleActor, 5 seconds, Shutdown)
      Await.result(stopped, 6 seconds)
    } catch {
      case e: Exception =>
    }
  }
}
