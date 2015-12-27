package com.learning.akka.remoting

import akka.actor.{Props, ActorRef, Actor, ActorSystem}
import com.typesafe.config.ConfigFactory

import scala.collection.concurrent.TrieMap

case class RegisterWorker(workerId: String)
case class RegisterWorkerSuccess(message: String)
class Master(actorSystem: ActorSystem) {
  val workers: TrieMap[String, ActorRef] = TrieMap[String, ActorRef]()
  val supervisor = actorSystem.actorOf(Props(new MasterActor), "Master")

  class MasterActor extends Actor{
    override def preStart() = {
      println(s"MasterActor started ${self.path.toString}")
    }
    def receive = {
      case RegisterWorker(workerId) =>
        println("register worker: " + workerId)
        workers += workerId -> sender()
        sender() ! RegisterWorkerSuccess("Register Success")
        println(workers)
    }
  }
}

object Master {
  def main(args: Array[String]) {
    val config = ConfigFactory.load("remote").getConfig("master")
    val system = ActorSystem("TestService", config)
    val master = new Master(system)
  }
}