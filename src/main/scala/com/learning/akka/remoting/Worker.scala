package com.learning.akka.remoting

import java.util.UUID

import akka.actor.{ActorRef, Props, Actor, ActorSystem}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._

class Worker(actorSystem: ActorSystem) {
  val duration: FiniteDuration = 15 seconds
  implicit val timeout = Timeout(duration)
  val workerId = UUID.randomUUID().toString
  val master = { Await.result(
    actorSystem.actorSelection("akka.tcp://TestService@127.0.0.1:8087/user/Master").resolveOne(),
    duration
  )
  }
  val supervisor = actorSystem.actorOf(Props(new WorkerActor), "Worker")

  class WorkerActor extends Actor {
    override def preStart() = {
      println(s"WorkerActor started ${self.path.address}")
      register()
//      master ! RegisterWorker(workerId)
    }

    def receive = {
      case RegisterWorkerSuccess(msg) =>
        println(msg)
    }
  }

  // can't send message here, can't refer sender()
  // must send message in Actor
  def register()(implicit sender: ActorRef) = {
    master ! RegisterWorker(workerId)
  }
}

object Worker {
  def main(args: Array[String]) {
    val config = ConfigFactory.load("remote").getConfig("worker")
    val system = ActorSystem("TestWorker", config)
    val worker = new Worker(system)
  }
}

