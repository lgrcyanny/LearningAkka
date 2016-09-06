package com.learning.akka.fault.tolerance

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Props, ActorSystem, OneForOneStrategy, Actor}
import akka.event.LoggingReceive
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern._
/**
 * Created by lgrcyanny on 15/12/28.
 */
class FaultActor extends Actor {
  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 5 seconds) {
    case _: RuntimeException => Stop
  }
  def receive = LoggingReceive {
    case "hello" => println("hello")
    case "error" =>
      throw new RuntimeException("error occurs")
  }
}

object FaultActor {
  def main(args: Array[String]) {
    implicit val timeout = Timeout(5 seconds)
    val config = ConfigFactory.parseString(
      """
        |akka.loglevel = "DEBUG"
        |akka.actor.debug {
        | receive = on
        | lifecycle = on
        |}
      """.stripMargin)
    val actorSystem = ActorSystem("FaultToleranceSample", config)
    val actor = actorSystem.actorOf(Props[FaultActor], "FaultActor")
    actor ! "hello"
    actor ! "error"
  }
}
