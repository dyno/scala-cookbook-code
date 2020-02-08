import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("hello back at you")
    case _ => println("huh?")
  }
}

object Main extends App {
  // an actor needs an ActorSystem
  val system = ActorSystem("HelloSystem")

  // create and start the actor, ActorRef
  val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")

  // send the actor two messages
  helloActor ! "hello"
  helloActor ! "buenos dias"

  // terminate the system
  system.terminate
}
