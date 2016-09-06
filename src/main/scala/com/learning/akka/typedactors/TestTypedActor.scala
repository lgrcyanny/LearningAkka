package com.learning.akka.typedactors

import akka.actor.{TypedProps, TypedActor, ActorSystem}

trait Processor {
  def run(i: Int): Unit
}

class ProcessorImpl extends Processor {
  def run(i: Int): Unit = {
    println(s"Process $i")
  }
}

class TestTypedActor {

}

object TestTypedActor {
  def main(args: Array[String]) {
    val system = ActorSystem("TypedActorSystem")
    val typedActorSystem = TypedActor(system)
    val actor: Processor = typedActorSystem.typedActorOf(TypedProps(classOf[ProcessorImpl], new ProcessorImpl), "Processor")
    val actorRef = typedActorSystem.getActorRefFor(actor)
    println(actorRef.path.toString)
    val proxy: Processor = typedActorSystem.typedActorOf(TypedProps(classOf[Processor]), actorRef)
    proxy.run(12)
    typedActorSystem.poisonPill(proxy)
  }
}
