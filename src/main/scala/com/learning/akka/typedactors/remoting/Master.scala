package com.learning.akka.typedactors.remoting

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._
import scala.collection.concurrent.TrieMap


trait Processor {
  def process(message: String): String
}

class ProcessorImpl extends Processor {
  def process(message: String): String = s"processed message: $message"
}

case class RegisterWorker(workerId: String)
case class DispatchWork(processorRef: ActorRef)
case object SendDispatchWork
class Master extends Actor {
  private val idToWorkers: TrieMap[String, ActorRef] = TrieMap[String, ActorRef]()
  val duration: FiniteDuration = 5 seconds
  implicit val timeout = Timeout(duration)
  implicit val executionContext = context.system.dispatcher
  // Define typed actor here
  val processorActor = TypedActor(context.system).typedActorOf(TypedProps(classOf[Processor], new ProcessorImpl), "ProcessorImpl")
  val processorActorRef = TypedActor(context.system).getActorRefFor(processorActor)

  override def preStart() = {
    println(s"Master started: ${self.path}, ${self.path.address}")
  }

  def receive = {
    case RegisterWorker(workerId) =>
      println(s"register worker: $workerId")
      idToWorkers += workerId -> sender()

    case SendDispatchWork =>
      println("dispatch works to workers")
      for ((workerId, worker) <- idToWorkers) {
        println(s"dispatch work to $workerId")
        worker ! DispatchWork(processorActorRef)
      }
  }
  context.system.scheduler.schedule(duration, duration, self, SendDispatchWork)
}

object Master {
  def main(args: Array[String]) {
    val config = ConfigFactory.load("remote").getConfig("master")
    val system = ActorSystem("MasterSystem", config)
    val masterActor = system.actorOf(Props[Master], "Master")
  }
}
