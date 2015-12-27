package com.learning.akka.typedactors.remoting

import java.net.InetAddress
import java.util.UUID

import akka.actor._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.Await
import scala.concurrent.duration._

class Worker extends Actor {
  val duration = 30 seconds
  implicit val timeout = Timeout(30 seconds)
  val masterActor =
    Await.result(
      context.system.actorSelection("akka.tcp://MasterSystem@127.0.0.1:8087/user/Master").resolveOne(),
      duration
    )
  val workerId = UUID.randomUUID().toString + "_" + InetAddress.getLocalHost.getHostName

  val typeActorSystem = TypedActor(context.system)

  override def preStart() = {
    println(s"Worker started: $workerId, ${self.path}, ${self.path.address}")
    masterActor ! RegisterWorker(workerId)
  }

  def receive = {
    case DispatchWork(processorRef) =>
      println(s"receive dispatched work, args: ${processorRef.path}")
      doDispatchedWork(processorRef)
  }

  def doDispatchedWork(processorRef: ActorRef) = {
    val processorProxy: Processor = typeActorSystem.typedActorOf(TypedProps[Processor], processorRef)
    println(s"processor: ${typeActorSystem.getActorRefFor(processorProxy).path}")
    val message = processorProxy.process("work string")
    println(message)
  }
}

object Worker {
  def main(args: Array[String]) {
    val config = ConfigFactory.load("remote").getConfig("worker")
    val system = ActorSystem("WorkerSystem", config)
    val workerActor = system.actorOf(Props[Worker], "Worker")
  }
}
