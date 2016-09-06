package com.learning.akka

import akka.actor.{Props, Actor, ActorSystem}
import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.duration._
import scala.collection.JavaConversions._

object TestKitUsageSpec {
  class EchoActor extends Actor {
    def receive = {
      case msg =>
        println(sender().path.toString)
        sender() ! msg
    }
  }
  val config =
    """
      |akka {
      |   loglevel = "WARNING"
      |}
    """.stripMargin
}

/**
 * Created by lgrcyanny on 16/1/11.
 */
class TestKitUsageSpec extends TestKit(ActorSystem("TestKitActorSystem", ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  import TestKitUsageSpec._
  val echoRef = system.actorOf(Props[EchoActor], "EchoActor")

  override def afterAll(): Unit = {
    shutdown()
  }

  "An EchoActor" should {
    "respond same message when it receive" in {
      within(500 millis) {
        echoRef ! "test"
        expectMsg("test")
      }
    }
  }
}
