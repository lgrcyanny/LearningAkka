package com.learning.akka.actors

import akka.actor.{Props, ActorSystem, Stash, Actor}

/**
 * Created by lgrcyanny on 15/12/23.
 */
class StashActor extends Actor with Stash {
  def receive = {
    case "foo" =>
      unstashAll()
      context.become({
        case "write" => println("processing write")
        case "close" =>
          unstashAll()
          context.unbecome()
        case msg =>
          stash()
      }, discardOld = false) //stack on top instead of replacing
    case msg =>
      stash()
  }
}

object StashActor {
  def main(args: Array[String]) {
    val system = ActorSystem("StashSystem")
    val stashActor = system.actorOf(Props[StashActor], "StashActor")
    stashActor ! "write"
    stashActor ! "write"
    stashActor ! "foo"
    stashActor ! "write"
    stashActor ! "close"
  }
}
