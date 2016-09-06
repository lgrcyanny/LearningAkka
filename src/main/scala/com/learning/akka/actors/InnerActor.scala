package com.learning.akka.actors

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

/**
 * Created by lgrcyanny on 15/11/6.
 */
case object Success
case object Fail
class InnerActor(actorSystem: ActorSystem) {
  val data = TrieMap("a" -> "b", "c" -> "d")
  val worker = actorSystem.actorOf(Props(new Actor{
    override def preStart = {
      println("start actor tester")
    }
    override def receive = {
      case msg@Success =>
        data += ("success" -> "success")
        println(msg + s", data: $data")
        sender() ! "Success"
      case msg@Fail =>
        data += ("fail" -> "fail")
        println(msg + s" data: $data")
        throw new RuntimeException("error")
    }
  }), "ActorTester")
}

object InnerActor {
  def main(args: Array[String]) {
    val config = ConfigFactory.parseString(
      """
        |akka.log-dead-letters = true
        |akka.log-dead-letters-during-shutdown = true
      """.stripMargin)
    val actorSystem = ActorSystem("ActorTesterSystem")
    implicit val ec = ExecutionContext.global
    val duration = Duration(5, TimeUnit.SECONDS)
    implicit val timeout = Timeout(duration)
    val tester = new InnerActor(actorSystem)
    val actorRef: ActorRef = Await.result(
      actorSystem.actorSelection("akka://ActorTesterSystem/user/ActorTester").resolveOne(),
      duration
    )
    actorRef ! Success
    actorRef ! Fail
    println(tester.data)
    actorRef ! Success
    actorRef ! Fail
    println(tester.data)
  }
}
