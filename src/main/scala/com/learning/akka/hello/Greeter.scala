package com.learning.akka.hello

import akka.actor.Actor

/**
 * Created by lgrcyanny on 15/10/7.
 */
object Greeter {
  case object Greet
  case object Done
}
class Greeter extends Actor {
  def receive = {
    case msg@Greeter.Greet =>
      println(s"Hello Akka, receive $msg")
      sender() ! Greeter.Done
  }
}
