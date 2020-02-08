// 13.1. Getting Started with a Simple Actor
// sbt run

// 13.2. Creating an Actor Whose Class Constructor Requires Arguments

import $ivy.`com.typesafe.akka::akka-actor:2.6.3`

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

// (1) constructor changed to take a parameter
class HelloActor(myName: String) extends Actor {
  def receive = {
    // (2) println statements changed to show the name
    case "hello" => println(s"hello from $myName")
    case _ => println(s"'huh?', said $myName")
  }
}

object Main extends App {
  // an actor needs an ActorSystem
  val system = ActorSystem("HelloSystem")

  // an actor whose constructor takes one argument
  val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")

  // send the actor two messages
  helloActor ! "hello"
  helloActor ! "buenos dias"

  // terminate the system
  system.terminate
}
// 13.3. How to Communicate Between Actors

import akka.actor._
case object PingMessage
case object PongMessage
case object StartMessage
case object StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def incrementAndPrint {
    count += 1
    println("ping")
  }

  def receive = {
    case StartMessage =>
      incrementAndPrint
      pong ! PingMessage
    case PongMessage =>
      incrementAndPrint
      if (count > 99) {
        sender ! StopMessage
        println("ping stopped")
        context.stop(self)
      } else {
        sender ! PingMessage
      }
    case _ => println("Ping got something unexpected.")
  }
}

class Pong extends Actor {
  def receive = {
    case PingMessage =>
      println(" pong")
      sender ! PongMessage
    case StopMessage =>
      println("pong stopped")
      context.stop(self)
    case _ => println("Pong got something unexpected.")
  }
}

object PingPongTest extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")

  // start the action
  ping ! StartMessage

  system.terminate
}

PingPongTest.main(Array())

// 13.4. Understanding the Methods in the Akka Actor Lifecycle

import akka.actor._

case object ForceRestart

class Kenny extends Actor {

  println("entered the Kenny constructor")
  override def preStart { println("kenny: preStart") }

  override def postStop { println("kenny: postStop") }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("kenny: preRestart")
    println(s" MESSAGE: ${message.getOrElse("")}")
    println(s" REASON: ${reason.getMessage}")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable) {
    println("kenny: postRestart")
    println(s" REASON: ${reason.getMessage}")
    super.postRestart(reason)
  }

  def receive = {
    case ForceRestart => throw new Exception("Boom!")
    case _ => println("Kenny received a message")
  }
}

object LifecycleDemo extends App {
  val system = ActorSystem("LifecycleDemo")
  val kenny = system.actorOf(Props[Kenny], name = "Kenny")

  println("sending kenny a simple String message")
  kenny ! "hello"
  Thread.sleep(1000)

  println("make kenny restart")
  kenny ! ForceRestart
  Thread.sleep(1000)

  println("stopping kenny")
  system.stop(kenny)

  println("shutting down system")
  system.terminate
}

LifecycleDemo.main(Array())

// 13.5. Starting an Actor

class HelloActor extends Actor {
  val myName = "Q"

  def receive = {
    // (2) println statements changed to show the name
    case "hello" => println(s"hello from $myName")
    case _ => println(s"'huh?', said $myName")
  }
}

val system = ActorSystem("HelloSystem")

// the actor is created and started here
val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")
helloActor ! "hello"

class Parent extends Actor {
  val myName = "P"
  val q = context.actorOf(Props[HelloActor], name = "Q")

  def receive = {
    case "hello" => println(s"hello from $myName")
    case s: String if s.startsWith("Q:") => q ! s.drop(2)
    case _ => println(s"'huh?', said $myName")
  }
}
val p = system.actorOf(Props[Parent], name = "P")

system.terminate


package actortests.parentchild

import akka.actor._

case class CreateChild(name: String)
case class Name(name: String)

class Child extends Actor {
  var name = "No name"
  override def postStop {

    println(s"D'oh! They killed me ($name): ${self.path}")
  }

  def receive = {
    case Name(name) => this.name = name
    case _ => println(s"Child $name got message")
  }
}

class Parent extends Actor {
  def receive = {
    case CreateChild(name) =>
      // Parent creates a new Child here

      println(s"Parent about to create Child ($name) ...")
      val child = context.actorOf(Props[Child], name = s"$name")
      child ! Name(name)
    case _ =>
      println(s"Parent got some other message.")
  }
}

object ParentChildDemo extends App {
  val actorSystem = ActorSystem("ParentChildTest")
  val parent = actorSystem.actorOf(Props[Parent], name = "Parent")

  // send messages to Parent to create to child actors
  parent ! CreateChild("Jonathan")
  parent ! CreateChild("Jordan")

  Thread.sleep(500)
  // lookup Jonathan, then kill it

  println("Sending Jonathan a PoisonPill ...")
  val jonathan = actorSystem.actorSelection("/user/Parent/Jonathan")
  jonathan ! PoisonPill

  println("jonathan was killed")
  Thread.sleep(5000)
  actorSystem.terminate
}

ParentChildDemo.main(Array())


// 13.6. Stopping Actors

package actortests

import akka.actor._

class TestActor extends Actor {
  def receive = {
    case _ =>
      println("a message was received")
  }
}

object SystemStopExample extends App {
  val actorSystem = ActorSystem("SystemStopExample")
  val actor = actorSystem.actorOf(Props[TestActor], name = "test")

  actor ! "hello"

  // Thread.sleep(10)
  // stop our actor
  actorSystem.stop(actor)

  actorSystem.terminate
}

SystemStopExample.main(Array())

// PoisonPill
package actortests

import akka.actor._

class TestActor extends Actor {
  def receive = {
    case s: String => println("Message Received: " + s)
    case _ => println("TestActor got an unknown message")
  }
  override def postStop { println("TestActor::postStop called") }
}

object PoisonPillTest extends App {
  val system = ActorSystem("PoisonPillTest")
  val actor = system.actorOf(Props[TestActor], name = "test")
  // a simple message
  actor ! "before PoisonPill"

  // the PoisonPill
  actor ! PoisonPill

  // these messages will not be processed
  actor ! "after PoisonPill"
  actor ! "hello?!"

  system.terminate
}

PoisonPillTest.main(Array())

// gracefulStop

package actortests.gracefulstop

import akka.actor._
import akka.pattern.gracefulStop
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

class TestActor extends Actor {
  def receive = { case _ => println("TestActor got message") }
  override def postStop { println("TestActor: postStop") }
}

object GracefulStopTest extends App {
  val system = ActorSystem("GracefulStopTest")
  val testActor = system.actorOf(Props[TestActor], name = "TestActor")

  // try to stop the actor gracefully
  try {
    val stopped: Future[Boolean] = gracefulStop(testActor, 2 seconds)
    Await.result(stopped, 3 seconds)
    println("testActor was stopped")
  } catch {
    case e: Exception => e.printStackTrace
  } finally {
    system.terminate
  }
}

GracefulStopTest.main(Array())

// “Killing” an actor

package actortests

import akka.actor._

class Number5 extends Actor {
  def receive = {
    case _ => println("Number5 got a message")
  }
  override def preStart { println("Number5 is alive") }
  override def postStop { println("Number5::postStop called") }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("Number5::preRestart called")
  }
  override def postRestart(reason: Throwable) {
    println("Number5::postRestart called")
  }
}

object KillTest extends App {
  val system = ActorSystem("KillTestSystem")
  val number5 = system.actorOf(Props[Number5], name = "Number5")

  number5 ! "hello"

  // send the Kill message
  number5 ! Kill

  system.terminate
}

KillTest.main(Array())

// 13.7. Shutting Down the Akka Actor System

object Main extends App {
  // create the ActorSystem
  val system = ActorSystem("HelloSystem")
  // put your actors to work here ...
  // shut down the ActorSystem when the work is finished
  system.terminate
}

Main.main(Array())

// 13.8. Monitoring the Death of an Actor with watch

package actortests.deathwatch

import akka.actor._

class Kenny extends Actor {
  def receive = {
    case _ =>
      println("Kenny received a message")
  }
}

class Parent extends Actor {
  // start Kenny as a child, then keep an eye on it
  val kenny = context.actorOf(Props[Kenny], name = "Kenny")

  context.watch(kenny)

  def receive = {
    case Terminated(kenny) => println(s"OMG, they killed $kenny")
    case _ => println("Parent received a message")
  }
}

// XXX: not sure why but wrapped in App does not work in ammonite this time.
// object DeathWatchTest extends App {
// create the ActorSystem instance
val system = ActorSystem("DeathWatchTest")

// create the Parent that will create Kenny
val parent = system.actorOf(Props[Parent], name = "Parent")

parent ! "a message"

// lookup kenny, then kill it
val kenny = system.actorSelection("akka://DeathWatchTest/user/Parent/Kenny")
kenny ! "a message"
kenny ! PoisonPill
Thread.sleep(5000)

println("calling system.terminate")
system.terminate
// }
// DeathWatchTest.main(Array())

case object Explode
class Kenny extends Actor {
  def receive = {
    case Explode => throw new Exception("Boom!")
    case _ => println("Kenny received a message")
  }

  override def preStart { println("kenny: preStart") }
  override def postStop { println("kenny: postStop") }
  override def preRestart(reason: Throwable, message: Option[Any]) {
    println("kenny: preRestart")
    super.preRestart(reason, message)
  }
  override def postRestart(reason: Throwable) {

    println("kenny: postRestart")
    super.postRestart(reason)
  }
}
val system = ActorSystem("DeathWatchTest")
val kenny = system.actorOf(Props[Kenny], name = "Kenny")
kenny ! "Hello?"
kenny ! Explode
kenny ! PoisonPill

// NOTE: https://stackoverflow.com/questions/22951549/how-do-you-replace-actorfor
val kenny = system.actorSelection("/user/Parent/Kenny")
// in a sibling actor
val kenny = context.actorSelection("../Kenny")
val kenny = system.actorSelection("akka://DeathWatchTest/user/Parent/Kenny")

// 13.9. Simple Concurrency with Futures


// Run one task, but block

package actors

// 1 - the imports
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Futures1 extends App {
  def sleep(time: Long) { Thread.sleep(time) }

  // used by 'time' method
  implicit val baseTime = System.currentTimeMillis

  // 2 - create a Future
  val f = Future {
    sleep(500)
    1 + 1
  }

  // 3 - this is blocking (blocking is bad)
  val result = Await.result(f, 1 second)

println(result)
  sleep(1000)
}

// Run one thing, but don’t block—use callback

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random

def sleep(time: Long) { Thread.sleep(time) }

object Example1 extends App {
  println("starting calculation ...")
  val f = Future {
    sleep(Random.nextInt(500))
    42
  }

  println("before onComplete")
  f.onComplete {
    case Success(value) =>
      println(s"Got the callback, meaning = $value")
    case Failure(e) => e.printStackTrace
  }

  // do the rest of your work

  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)
  sleep(2000)
}
Example1.main(Array())

// The onSuccess and onFailure callback methods

import scala.concurrent.{Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random

object OnSuccessAndFailure extends App {
  val f = Future {
    sleep(Random.nextInt(500))
    if (Random.nextInt(500) > 250) throw new Exception("Yikes!") else 42
  }
  f onSuccess {
    case result =>
      println(s"Success: $result")
  }
  f onFailure {
    case t =>
      println(s"Exception: ${t.getMessage}")
  }

  // do the rest of your work

  println("A ..."); sleep(100)
  println("B ..."); sleep(100)
  println("C ..."); sleep(100)
  println("D ..."); sleep(100)
  println("E ..."); sleep(100)
  println("F ..."); sleep(100)
  sleep(2000)
}

OnSuccessAndFailure.main(Array())

// Creating a method to return a Future[T]

import scala.concurrent.{Await, Future, future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Futures2 extends App {
  implicit val baseTime = System.currentTimeMillis

  def longRunningComputation(i: Int): Future[Int] = future {
    sleep(100)
    i + 1
  }

  // this does not block
  longRunningComputation(11).onComplete {
    case Success(result) => println(s"result = $result")
    case Failure(e) => e.printStackTrace
  }
}

Futures2.main(Array())

// Run multiple things; something depends on them; join them together

import scala.concurrent.{Future, future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.util.Random

object Cloud {
  def runAlgorithm(i: Int): Future[Int] = future {
    sleep(Random.nextInt(500))
    val result = i + 10
    println(s"returning result from cloud: $result")
    result
  }
}

object RunningMultipleCalcs extends App {
  println("starting futures")
  val result1 = Cloud.runAlgorithm(10)
  val result2 = Cloud.runAlgorithm(20)
  val result3 = Cloud.runAlgorithm(30)
  println("before for-comprehension")
  val result = for {
    r1 <- result1
    r2 <- result2
    r3 <- result3
  } yield (r1 + r2 + r3)

  println("before onSuccess")
  result onSuccess {
    case result => println(s"total = $result")
  }
  println("before sleep at the end")
}

RunningMultipleCalcs.main(Array())

val calculateMeaningOfLife = () => { Thread.sleep(2000); 84 }
val meaning = Future { calculateMeaningOfLife() } fallbackTo Future { 42 }
meaning andThen {
  case Success(meaning) => println(s"meaning of life is $meaning")
}

// 13.10. Sending a Message to an Actor and Waiting for a Reply

// import $ivy.`com.typesafe.akka::akka-actor:2.4.20`
import $ivy.`com.typesafe.akka::akka-actor:2.6.3`

// https://stackoverflow.com/questions/37501378/akka-2-4-6-where-is-ask-or-method

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

case object AskNameMessage
class TestActor extends Actor {
  def receive = {
    case AskNameMessage =>
      // respond to the 'ask' request
      sender ! "Fred"
    case _ => println("that was unexpected")
  }
}

object AskTest extends App {
  // create the system and actor
  val system = ActorSystem("AskTestSystem")
  val myActor = system.actorOf(Props[TestActor], name = "myActor")

  // (1) this is one way to "ask" another actor for information
  implicit val timeout = Timeout(5 seconds)
  val future = myActor ? AskNameMessage
  val result = Await.result(future, timeout.duration).asInstanceOf[String]
  println(result)

  // (2) a slightly different way to ask another actor for information
  val future2: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
  val result2 = Await.result(future2, 1 second)
  println(result2)

  system.terminate
}

AskTest.main(Array())

// 13.11. Switching Between Different States with become

package actortests.becometest

import akka.actor._

case object ActNormalMessage
case object TryToFindSolution
case object BadGuysMakeMeAngry

class DavidBanner extends Actor {
  import context._

  def angryState: Receive = {
    case ActNormalMessage =>
      println("Phew, I'm back to being David.")
      become(normalState)
  }

  def normalState: Receive = {
    case TryToFindSolution => println("Looking for solution to my problem ...")
    case BadGuysMakeMeAngry =>
      println("I'm getting angry...")
      become(angryState)
  }

  def receive = {
    case BadGuysMakeMeAngry => become(angryState)
    case ActNormalMessage => become(normalState)
  }
}

object BecomeHulkExample extends App {
  val system = ActorSystem("BecomeHulkExample")
  val davidBanner = system.actorOf(Props[DavidBanner], name = "DavidBanner")
  davidBanner ! ActNormalMessage // init to normalState
  davidBanner ! TryToFindSolution
  davidBanner ! BadGuysMakeMeAngry

  Thread.sleep(1000)
  davidBanner ! ActNormalMessage

  system.terminate
}

BecomeHulkExample.main(Array())


// 13.12. Using Parallel Collections

// XXX: https://github.com/lihaoyi/Ammonite/issues/556, Parallel map hangs indefinitely

object ParTest extends App {
  val v = Vector.range(0, 10)

  v.foreach(print)
  v.par.foreach(print)
  v.par.foreach(print)
  v.par.foreach { e =>
    print(e)
    Thread.sleep(50)
  }
}

ParTest.main(Array())


import scala.collection.parallel.immutable.ParVector

object ParTest2 extends App {
  val v = ParVector.range(0, 10)

  v.foreach { e =>
    Thread.sleep(100)
    print(e)
  }
}

ParTest2.main(Array())
