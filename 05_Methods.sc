// scala
def doSomething(x: Int): String = {
   "code here"
}

def plusOne(i: Int) = i + 1

// 5.1 Controlling Method Scope

class Foo {
  private[this] def isFoo = true
  def doFoo(other: Foo) {
    if (other.isFoo) { // this line won't compile
      // ...
    }
  }
}

class Foo {
  private def isFoo = true
  def doFoo(other: Foo) {
    if (other.isFoo) { // this now compiles
      // ...
    }
  }
}

{
  class Animal {
    private def heartBeat {}
  }
  class Dog extends Animal {
    heartBeat // won't compile
  }
}

{
  class Animal {
    protected def breathe {}
  }
  class Dog extends Animal {
    breathe
  }
}

package world
{
  class Animal {
    protected def breathe {}
  }
  class Jungle {
    val a = new Animal
    a.breathe // error: this line won't compile
  }
}

package com.acme.coolapp.model
{
  class Foo {
    private[model] def doX {}
    private def doY {}
  }
  class Bar {
    val f = new Foo
    f.doX // compiles f.doY // won't compile
  }
}

package com.acme.coolapp.model
{
  class Foo {
    private[model] def doX {}
    private[coolapp] def doY {}
    private[acme] def doZ {}
  }
}
import com.acme.coolapp.model._
package com.acme.coolapp.view
{
  class Bar {
    val f = new Foo
    f.doX // won't compile
    f.doY
    f.doZ
  }
}
package com.acme.common
{
  class Bar {
    val f = new Foo
    f.doX // won't compile
    f.doY // won't compile
    f.doZ
  }
}

package com.acme.coolapp.model
{
  class Foo {
    def doX {}
  }
}
package org.xyz.bar
{
  class Bar {
    val f = new com.acme.coolapp.model.Foo
    f.doX
  }
}

// 5.2 Calling a Method on a Superclass

class WelcomeActivity extends Activity {
  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    // more code here ...
  }
}

{
  class FourLeggedAnimal {
    def walk { println("I'm walking") }
    def run { println("I'm running") }
  }

  class Dog extends FourLeggedAnimal {
    def walkThenRun {
      super.walk
      super.run
    }
  }

  val suka = new Dog
  suka.walkThenRun
}

{
  trait Human { def hello = "the Human trait" }
  trait Mother extends Human { override def hello = "Mother" }
  trait Father extends Human { override def hello = "Father" }

  class Child extends Human with Mother with Father {
    def printSuper = super.hello
    def printMother = super[Mother].hello
    def printFather = super[Father].hello
    def printHuman = super[Human].hello
  }

  object Test extends App {
    val c = new Child
    println(s"c.printSuper = ${c.printSuper}")
    println(s"c.printMother = ${c.printMother}")
    println(s"c.printFather = ${c.printFather}")
    println(s"c.printHuman = ${c.printHuman}")
  }

  Test.main(Array())
}

{
  trait Animal {
    def walk { println("Animal is walking") }
  }
  class FourLeggedAnimal extends Animal {
    override def walk { println("I'm walking on all fours") }
  }
  class Dog extends FourLeggedAnimal {
    def walkThenRun {
      super.walk // works
      super[FourLeggedAnimal].walk // works
      super[Animal].walk // error: won't compile
    }
  }
}

// 5.3 Setting Default Values for Method Parameters

{
  class Connection {
    def makeConnection(timeout: Int = 5000, protocol: String = "http") {
      println("timeout = %d, protocol = %s".format(timeout, protocol))
      // more code here
    }
  }

  val c = new Connection
  c.makeConnection()
  c.makeConnection(2000)
  c.makeConnection(3000, "https")

  c.makeConnection(timeout=10000)
  c.makeConnection(protocol="https")
  c.makeConnection(timeout=10000, protocol="https")
}

// 5.4 Using Parameter Names When Calling a Method

{
  class Pizza {
    var crustSize = 12
    var crustType = "Thin"
    def update(crustSize: Int, crustType: String) {
      this.crustSize = crustSize
      this.crustType = crustType
    }
    override def toString = {
      "A %d inch %s crust pizza.".format(crustSize, crustType)
    }
  }

  val p = new Pizza
  p.update(crustSize = 16, crustType = "Thick")
  p.update(crustType = "Pan", crustSize = 14)
}

// 5.5 Defining a Method That Returns Multiple Items (Tuples)
{
  def getStockInfo = {
  // other code here ...
    ("NFLX", 100.00, 101.00) // this is a Tuple3
  }
  val (symbol, currentPrice, bidPrice) = getStockInfo
  val result = getStockInfo
  result._1
  result._2
}

// 5.6 Forcing Callers to Leave Parentheses off Accessor Methods
{
  class Pizza {
// no parentheses after crustSize
    def crustSize = 12
  }

  val p = new Pizza
  p.crustSize
}

// 5.7 Creating Methods That Take Variable-Argument Fields
{
  def printAll(strings: String*) {
    strings.foreach(println)
  }
  // these all work
  printAll()
  printAll("foo")
  printAll("foo", "bar")
  printAll("foo", "bar", "baz")
}

{
  // a sequence of strings
  val fruits = List("apple", "banana", "cherry")
  // pass the sequence to the varargs field
  printAll(fruits: _*)
}

{
  def printAll(numbers: Int*) { numbers.foreach(println) }
  def printAll(numbers: Int*) { println(numbers.getClass) }
  printAll(1, 2, 3)
  printAll()
}

// 5.8 Declaring That a Method Can Throw an Exception
@throws(classOf[Exception])
override def play {
  // exception throwing code here ...
}

@throws(classOf[IOException])
@throws(classOf[LineUnavailableException])
@throws(classOf[UnsupportedAudioFileException])
def playSoundFileWithJavaAudio {
  // exception throwing code here
}

{
  object BoomTest extends App {
    def boom { throw new Exception }
    println("Before boom")
    boom
    // this line is never reached
    println("After boom")
  }
  BoomTest.main(Array())
}

// 5.9 Supporting a Fluent Style of Programming
{
  class Person {
    protected var fname = ""
    protected var lname = ""
    def setFirstName(firstName: String): this.type = {
      fname = firstName
      this
    }
    def setLastName(lastName: String): this.type = {
      lname = lastName
      this
    }
  }

  class Employee extends Person {
    protected var role = ""
    def setRole(role: String): this.type = {
      this.role = role
      this
    }
    override def toString = {
      "%s, %s, %s".format(fname, lname, role)
    }
  }

  object Main extends App {
    val employee = new Employee
    // use the fluent methods
    employee
      .setFirstName("Al")
      .setLastName("Alexander")
      .setRole("Developer")
    println(employee)
  }

  Main.main(Array())
}

{
  import scala.collection.mutable.ArrayBuffer

  final class Pizza {
    private val toppings = ArrayBuffer[String]()
    private var crustSize = 0
    private var crustType = ""

    def addTopping(topping: String) = {
      toppings += topping
      this
    }
    def setCrustSize(crustSize: Int) = {
      this.crustSize = crustSize
      this
    }
    def setCrustType(crustType: String) = {
      this.crustType = crustType
      this
    }
    def print() {
      println(s"crust size: $crustSize")
      println(s"crust type: $crustType")
      println(s"toppings: $toppings")
    }
  }

  object FluentPizzaTest extends App {
    val p = new Pizza

    p.setCrustSize(14)
      .setCrustType("thin")
      .addTopping("cheese")
      .addTopping("green olives")
      .print()
  }

  FluentPizzaTest.main(Array())
}