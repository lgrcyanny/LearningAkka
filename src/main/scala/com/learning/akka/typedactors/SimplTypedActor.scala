package com.learning.akka.typedactors

import akka.actor._

import scala.concurrent.Future


/**
 * Created by lgrcyanny on 15/12/25.
 */
trait Foo {
  def doFoo(times: Int): Unit = println(s"Foo $times")
  def hello(string: String): Unit = println(string)
}

trait Bar {
  def doBar(str: String): Future[String] = Future.successful(str.toUpperCase)
}

class FooBar extends Foo with Bar

class Hello extends Actor {
  override def preStart() = {
    println("HelloActor started")
  }
  def receive = {
    case msg@"hello" => println(msg)
  }
}

object SimplTypedActor {
  def main(args: Array[String]) {
    val system = ActorSystem("TypedActorSystem")
    val helloActor = system.actorOf(Props[Hello], "HelloActor")
    val fooAndBar: Foo with Bar = TypedActor(system).typedActorOf(TypedProps[FooBar]())
    val fooAndBarActorRef = TypedActor(system).getActorRefFor(fooAndBar)

    val fooAndBarProxy: Foo with Bar = TypedActor(system).typedActorOf(TypedProps[FooBar], fooAndBarActorRef)

    println(TypedActor(system).getActorRefFor(fooAndBarProxy).path.toString)
    fooAndBarProxy.doFoo(1)
    fooAndBarProxy.hello("hello")
  }
}
