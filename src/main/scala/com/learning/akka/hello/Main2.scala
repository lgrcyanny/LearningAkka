package com.learning.akka.hello

import akka.actor.ActorSystem.Settings
import akka.actor._
import akka.dispatch.{Dispatchers, Mailboxes}
import akka.event.{LoggingAdapter, EventStream}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.Duration

/**
 * Created by lgrcyanny on 15/10/7.
 */
object Main2 {

  def main(args: Array[String]) {
    val system = ActorSystem("HelloWorld")
    val helloWorld = system.actorOf(Props[HelloWorld], "helloWorld")
    val terminator = system.actorOf(Props(classOf[Terminator], helloWorld), "terminator")
  }
}

class Terminator(ref: ActorRef) extends Actor with ActorLogging {
  context watch ref
  def receive = {
    case Terminated(_) =>
      log.info(s"${ref.path} has terminated")
      context.system.stop(self)
  }
}
