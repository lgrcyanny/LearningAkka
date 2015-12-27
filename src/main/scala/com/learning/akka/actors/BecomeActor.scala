package com.learning.akka.actors

import akka.actor.{Props, ActorSystem, Actor}

/**
 * Created by lgrcyanny on 15/12/22.
 */
class BecomeActor extends Actor {
  def happy: Receive = {
    case "foo" => println("I am happy with foo")
    case "bar" => println("I am happy with bar")
  }
  def receive: Receive = {
    case "foo" => context.become(happy)
    case "bar" => println("I am bar")
  }
}

object BecomeActor {
  def main(args: Array[String]) {
    val system = ActorSystem("BecomeTest")
    val actor = system.actorOf(Props[BecomeActor], "BecomeActor")
    actor ! "bar"
    actor ! "foo"
    actor ! "foo"
    actor ! "foo"
  }
}
