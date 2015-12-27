package com.learning.akka.actors

import akka.actor._

/**
 * Created by lgrcyanny on 15/11/24.
 */
class WatchActor extends Actor {
  val child = context.actorOf(Props.empty, "child")
  context.watch(child)
  var lastSender: ActorRef = context.system.deadLetters
  def receive = {
    case "Kill" =>
      println(s"sender is ${sender().path}")
      lastSender = sender()
      context.stop(child)
    case Terminated(`child`) =>
      println("child terminated")
      lastSender ! "Finished"
  }
}

object WatchActor {
  def main(args: Array[String]) {
    val system = ActorSystem("TestSystem")
    val watchActor = system.actorOf(Props[WatchActor], "WatchActor")
    watchActor ! "Kill"
  }
}
