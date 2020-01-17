// 8.1
trait BaseSoundPlayer {
  def play
  def close
  def pause
  def stop
  def resume
}

trait Dog {
  def speak(whatToSay: String)
  def wagTail(enabled: Boolean)
}

class Mp3SoundPlayer extends BaseSoundPlayer {
  def play
  def close
  def pause
  def stop
  def resume
}

// must be declared abstract because it does not implement
// all of the BaseSoundPlayer methods
abstract class SimpleSoundPlayer extends BaseSoundPlayer {
  def play { /*...*/ }
  def close { /*...*/ }
}

trait Mp3BaseSoundFilePlayer extends BaseSoundFilePlayer {
  def getBasicPlayer: BasicPlayer
  def getBasicController: BasicController
  def setGain(volume: Double)
}

// 8.2

trait PizzaTrait {
  var numToppings: Int // abstract
  var size = 14 // concrete
  val maxNumToppings = 10 // concrete
}

class Pizza extends PizzaTrait {
  var numToppings = 0 // 'override' not needed
  size = 16 // 'var' and 'override' not needed
  override val maxNumToppings = 10 // 'override' is required
}

// 8.3
{
  trait Pet {
    // > Avoid the procedure syntax, as it tends to be confusing for very little gain in brevity.
    // https://docs.scala-lang.org/style/declarations.html
    def speak: Unit = { println("Yo") } // concrete implementation
    def comeToMaster: Unit // abstract method
  }

  class Dog extends Pet {
    // don't need to implement 'speak' if you don't need to
    def comeToMaster: Unit = { println("I'm coming!") }
  }

  class Cat extends Pet {
    // override the speak method
    override def speak: Unit = { println("meow") }
    def comeToMaster: Unit = { println("That's not gonna happen.") }
  }

  abstract class FlyingPet extends Pet {
    def fly: Unit = { println("I'm flying!") }
  }

}

// 8.4
{
  trait Tail {
    def wagTail { println("tail is wagging") }
    def stopTail { println("tail is stopped") }
  }

  abstract class Pet(var name: String) {
    def speak // abstract
    def ownerIsHome { println("excited") }
    def jumpForJoy { println("jumping for joy") }
  }

  class Dog(name: String) extends Pet(name) with Tail {
    def speak { println("woof") }
    override def ownerIsHome {
      wagTail
      speak
    }
  }

  object Test extends App {
    val zeus = new Dog("Zeus")
    zeus.ownerIsHome
    zeus.jumpForJoy
  }

  Test.main(Array())
}

// 8.5
{
  class StarfleetComponent
  trait StarfleetWarpCore extends StarfleetComponent
  class Starship extends StarfleetComponent with StarfleetWarpCore
}

{
  class StarfleetComponent
  trait StarfleetWarpCore extends StarfleetComponent
  class RomulanStuff
  // won't compile
  class Warbird extends RomulanStuff with StarfleetWarpCore
}

{
  abstract class Employee
  class CorporateEmployee extends Employee
  class StoreEmployee extends Employee
  trait DeliversFood extends StoreEmployee
  // this is allowed
  class DeliveryPerson extends StoreEmployee with DeliversFood
  // won't compile
  class Receptionist extends CorporateEmployee with DeliversFood
}

// 8.6
{
  trait StarfleetWarpCore { this: Starship =>
    // more code here ...
  }
  class Starship
  class Enterprise extends Starship with StarfleetWarpCore

  class RomulanShip
// this won't compile
  class Warbird extends RomulanShip with StarfleetWarpCore
}

{
  trait WarpCore {
    this: Starship with WarpCoreEjector with FireExtinguisher =>
  }

  class Starship
  trait WarpCoreEjector
  trait FireExtinguisher
  // this works
  class Enterprise extends Starship with WarpCore with WarpCoreEjector with FireExtinguisher
}

// 8.7

{
  trait WarpCore {
    this: { def ejectWarpCore(password: String): Boolean } =>
  }

  class Starship {
// code here ...
  }
  class Enterprise extends Starship with WarpCore {
    def ejectWarpCore(password: String): Boolean = {
      if (password == "password") { println("ejecting core"); true } else false
    }
  }
}

{
  trait WarpCore {
    this: {
      def ejectWarpCore(password: String): Boolean
      def startWarpCore: Unit
    } =>
  }
  class Starship
  class Enterprise extends Starship with WarpCore {
    def ejectWarpCore(password: String): Boolean = {
      if (password == "password") { println("core ejected"); true } else false
    }
    def startWarpCore { println("core started") }
  }
}

// 8.8
{
  class DavidBanner
  trait Angry {
    println("You won't like me ...")
  }

  object Test extends App {
    val hulk = new DavidBanner with Angry
  }

  Test.main(Array())
}

{
  trait Debugger {
    def log(message: String) {
      // do something with message
    }
  }
  // no debugger
  val child = new Child

  // debugger added as the object is created
  val problemChild = new ProblemChild with Debugger
}

// 8.9

```java
public interface Animal { public void speak(); }
public interface Wagging { public void wag(); }
public interface Running { public void run(); }
```

// scala
class Dog extends Animal with Wagging with Running {
  def speak { println("Woof") }
  def wag { println("Tail is wagging!") }
  def run { println("I'm running!") }
}
