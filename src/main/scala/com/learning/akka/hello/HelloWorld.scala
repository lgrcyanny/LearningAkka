package com.learning.akka.hello

import akka.actor.{Props, Actor}

/**
 * Created by lgrcyanny on 15/10/7.
 */
class HelloWorld extends Actor {
  override def preStart = {
    val greeter = context.actorOf(Props[Greeter], "greeter")
    greeter ! Greeter.Greet
  }

  override def receive = {
    case Greeter.Done =>
      context.stop(self)
  }
}
