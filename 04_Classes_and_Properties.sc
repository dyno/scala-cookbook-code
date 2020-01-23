// 4.1 Creating a Primary Constructor
class Person(var firstName: String, var lastName: String) {
  println("the constructor begins")
  // some class fields
  private val HOME = System.getProperty("user.home")
  var age = 0

  // some methods
  override def toString = s"$firstName $lastName is $age years old"
  def printHome { println(s"HOME = $HOME") }
  def printFullName { println(this) } // uses toString

  printHome
  printFullName
  println("still in the constructor")
}

val p = new Person("Adam", "Meyer")

p.firstName = "Scott"
p.lastName = "Jones"

println(p.firstName)
println(p.lastName)

p.age = 30
println(p.age)


object Test extends App {
  val p = new Person

  // the 'normal' mutator approach
  p.name = "Ron Artest"
  println(p)

  // the 'hidden' mutator method
  p.name_$eq("Metta World Peace")
  println(p)
}


// 4.2 Controlling the Visibility of Constructor Fields
class Person(var name: String)
val p = new Person("Alvin Alexander")
p.name
p.name = "Fred Flintstone"
p.name

class Person(val name: String)
val p = new Person("Alvin Alexander")
p.name

// default is very restricted
class Person(name: String)
val p = new Person("Alvin Alexander")
// p.name // => value name is not a member of ammonite.$sess.cmd228.Person

class Person(private var name: String) { def getName {println(name)} }
val p = new Person("Alvin Alexander")
// p.name // => variable name in class Person cannot be accessed in ammonite.$sess.cmd231.Person
p.getName

case class Person(name: String)
val p = Person("Dale Cooper")
p.name


// 4.3 Defining Auxiliary Constructors
// primary constructor
{
  class Pizza(var crustSize: Int, var crustType: String) {
    // one-arg auxiliary constructor
    def this(crustSize: Int) {
      this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
    }
    // one-arg auxiliary constructor
    def this(crustType: String) {
      this(Pizza.DEFAULT_CRUST_SIZE, crustType)
    }
    // zero-arg auxiliary constructor
    def this() {
      this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
    }
    override def toString = s"A $crustSize inch pizza with a $crustType crust"
  }

  object Pizza {
    val DEFAULT_CRUST_SIZE = 12
    val DEFAULT_CRUST_TYPE = "THIN"
  }

  val p1 = new Pizza(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
  val p2 = new Pizza(Pizza.DEFAULT_CRUST_SIZE)
  val p3 = new Pizza(Pizza.DEFAULT_CRUST_TYPE)
  val p4 = new Pizza
}
// initial case class
case class Person (var name: String, var age: Int)
val p = Person("John Smith", 30)
val p = Person.apply("John Smith", 30)

{
  // the case class
  case class Person(var name: String, var age: Int)
  // the companion object
  object Person {
    def apply() = new Person("<no name>", 0)
    def apply(name: String) = new Person(name, 0)
  }

  object CaseClassTest extends App {
    val a = Person() // corresponds to apply()
    val b = Person("Pam") // corresponds to apply(name: String)
    val c = Person("William Shatner", 82)
    println(a)
    println(b)
    println(c)
    // verify the setter methods work
    a.name = "Leonard Nimoy"
    a.age = 82
    println(a)
  }

  CaseClassTest.main(Array())
}

// 4.4 Defining a Private Primary Constructor
class Person private (name: String)
// val p = new Person("Mercedes")

{
  class Brain private {
    override def toString = "This is the brain."
  }

  object Brain {
    val brain = new Brain
    def getInstance = brain
  }

  object SingletonTest extends App {
    // this won't compile
    // val brain = new Brain
    // this works
    val brain = Brain.getInstance
    println(brain)
  }
}

{
  object FileUtils {
    def readFile(filename: String) = {
      // code here ...
    }
    def writeToFile(filename: String, contents: String) {
      // code here ...
    }
  }

  val contents = FileUtils.readFile("input.txt")
  FileUtils.writeToFile("output.txt", content)
}

// 4.5 Providing Default Values for Constructor Parameters
{
  class Socket(val timeout: Int = 10000)

  val s = new Socket
  s.timeout

  val s = new Socket(5000)
  s.timeout

  val s = new Socket(timeout = 5000)
  s.timeout

  class Socket(val timeout: Int) {
    def this() = this(10000)
    override def toString = s"timeout: $timeout"
  }
}

{
  class Socket(val timeout: Int = 1000, val linger: Int = 2000) {
    override def toString = s"timeout: $timeout, linger: $linger"
  }
  println(new Socket)
  println(new Socket(3000))
  println(new Socket(3000, 4000))
  println(new Socket(timeout=3000, linger=4000))
  println(new Socket(linger=4000, timeout=3000))
  println(new Socket(timeout=3000))
  println(new Socket(linger=4000))
}

// 4.6 Overriding Default Accessors and Mutators

{
  class Person(private var _name: String) {
    def name = _name // accessor
    def name_=(aName: String) { _name = aName } // mutator
  }

  val p = new Person("Jonathan")
  p.name = "Jony" // setter
  println(p.name) // getter
}

// intentionally left the 'private' modifier off _symbol
class Stock(var _symbol: String) { // getter
  def symbol = _symbol
  // setter
  def symbol_=(s: String) {
    this.symbol = s
    println(s"symbol was updated, new value is $symbol")
  }
}

// 4.7 Preventing Getter and Setter Methods from Being Generated

class Stock {
  // getter and setter methods are generated
  var delayedPrice: Double = _

  // keep this field hidden from other classes
  private var currentPrice: Double = _
}

{
  class Stock {
    // a private field can be seen by any Stock instance
    private var price: Double = _

    def setPrice(p: Double) { price = p }
    def isHigher(that: Stock): Boolean = this.price > that.price
  }

  object Driver extends App {
    val s1 = new Stock
    s1.setPrice(20)

    val s2 = new Stock
    s2.setPrice(100)

    println(s2.isHigher(s1))
  }

}

class Stock {
  // a private[this] var is object-private, and can only be seen
  // by the current instance
  private[this] var price: Double = _
  def setPrice(p: Double) { price = p }
  // error: this method won't compile because price is now object-private
  // => cmd262.sc:7: value price is not a member of ammonite.$sess.cmd262.Stock
  def isHigher(that: Stock): Boolean = this.price > that.price
}

// 4.8 Assigning a Field to a Block or Function
{
  class Foo {
    // set 'text' equal to the result of the block of code
    val text = {
      var lines = ""
      try {
        lines = io.Source.fromFile("/etc/passwd").getLines.mkString
      } catch {
        case e: Exception => lines = "Error happened"
      }
      lines
    }

    println(text)
  }

  object Test extends App {
    val f = new Foo
  }
}

class Foo {
  import scala.xml.XML
  // assign the xml field to the result of the load method
  val xml = XML.load("http://example.com/foo.xml")
  // more code here ...
}

{
  class Foo {
    val text =
      scala.io.Source.fromFile("/etc/passwd").getLines.foreach(println)
  }
  object Test extends App {
    val f = new Foo
  }

  Test.main(Array())
}

{
  class Foo {
    lazy val text =
      scala.io.Source.fromFile("/etc/passwd").getLines.foreach(println)
  }

  object Test extends App {
    val f = new Foo
  }

  Test.main(Array())
}

{
  case class Address(city: String, state: String, zip: String)
  case class Person(var username: String, var password: String) {
    var age = 0
    var firstName = ""
    var lastName = ""
    var address = None: Option[Address]
  }

  val p = Person("alvinalexander", "secret")
  p.address = Some(Address("Talkeetna", "AK", "99676"))
  p.address.foreach { a =>
    println(a.city)
    println(a.state)
    println(a.zip)
  }
}


var i = 0 // Int
var d = 0.0 // Double
var b:Byte =0
var c:Char =0
var f: Float = 0
var l:Long =0
var s: Short = 0

{
  class Person(var name: String, var address: Address) {
    override def toString = if (address == null) name else s"$name @ $address"
  }
  case class Address(city: String, state: String)

  // *var* age
  class Employee(name: String, address: Address, var age: Int) extends Person(name, address) {
    // rest of the class
  }

  val teresa = new Employee("Teresa", Address("Louisville", "KY"), 25)
  teresa.name
  teresa.address
  teresa.age
}

// 4.11 Calling a Superclass Constructor

{
  // (1) primary constructor
  class Animal(var name: String, var age: Int) {
    // (2) auxiliary constructor
    def this(name: String) {
      this(name, 0)
    }
    override def toString = s"$name is $age years old"
  }

  // calls the Animal one-arg constructor
  class Dog(name: String) extends Animal(name) {
    println("Dog constructor called")
  }

  // call the two-arg constructor
  class Dog(name: String) extends Animal(name, 0) {
    println("Dog constructor called")
  }
}

{
  case class Address(city: String, state: String)
  case class Role(role: String)
  class Person(var name: String, var address: Address) {
    // no way for Employee auxiliary constructors to call this constructor
    def this(name: String) {
      this(name, null)
      address = null
    }
    override def toString = if (address == null) name else s"$name @ $address"
  }
  class Employee(name: String, role: Role, address: Address) extends Person(name, address) {
    def this(name: String) {
      this(name, null, null)
    }
    def this(name: String, role: Role) {
      this(name, role, null)
    }
    def this(name: String, address: Address) {
      this(name, null, address)
    }
  }
}

// 4.12 When to Use an Abstract Class

// raits don’t allow constructor parameters:
// this won't compile
trait Animal(name: String)

abstract class Animal(name: String)

abstract class BaseController(db: Database) {
  def save { db.save }
  def update { db.update }
  def delete { db.delete }

  // abstract
  def connect
  // an abstract method that returns a String
  def getStatus: String
  // an abstract method that takes a parameter
  def setServerName(serverName: String)
}

// 4.13 Defining Properties in an Abstract Base Class (or Trait)
{
  abstract class Pet(name: String) {
    val greeting: String
    var age: Int
    def sayHello { println(greeting) }
    override def toString = s"I say $greeting, and I'm $age"
  }

  class Dog(name: String) extends Pet(name) {
    val greeting = "Woof"
    var age = 2
  }

  class Cat(name: String) extends Pet(name) {
    val greeting = "Meow"
    var age = 5
  }

  object AbstractFieldsDemo extends App {
    val dog = new Dog("Fido")
    val cat = new Cat("Morris")
    dog.sayHello
    cat.sayHello
    println(dog)
    println(cat)
    // verify that the age can be changed
    cat.age = 10
    println(cat)
  }

  AbstractFieldsDemo.main(Array())
}

{
  // abstract class def, concrete class val.
  abstract class Pet(name: String) {
    def greeting: String
  }
  class Dog(name: String) extends Pet(name) {
    v
    al greeting = "Woof"
  }
  object Test extends App {
    val dog = new Dog("Fido")
    println(dog.greeting)
  }
}

{
  abstract class Animal {
    val greeting = "Hello"
    def sayHello { println(greeting) }
    def run
  }
  class Dog extends Animal {
    override val greeting = "Woof"
    def run {
      println("Dog is running")
    }
  }
}

{
  abstract class Animal {
    val greeting = { println("Animal"); "Hello" }
  }
  class Dog extends Animal {
    override val greeting = {
      println("Dog"); "Woof"
    }
  }
  object Test extends App {
    new Dog
  }

  Test.main(Array())
}

{
  abstract class Animal {
    final val greeting = "Hello" // made the field 'final'
  }
  class Dog extends Animal {
    val greeting = "Woof" // this line won't compile
  }
}

{
  abstract class Animal {
    var greeting = "Hello"
    var age = 0
    override def toString = s"I say $greeting, and I'm $age years old."
  }

  class Dog extends Animal {
    greeting = "Woof"
    age = 2
  }
}

{
  trait Animal {
    val greeting: Option[String]
    var age: Option[Int] = None
    override def toString = s"I say $greeting, and I'm $age years old."
  }

  class Dog extends Animal {
    val greeting = Some("Woof")
    age = Some(2)
  }

  object Test extends App {
    val d = new Dog
    println(d)
  }

  Test.main(Array())
}

// 4.14 Generating Boilerplate Code with Case Classes
{
  case class Person(name: String, relation: String)
  val emily = Person("Emily", "niece")
  emily.name
  emily.name = "Fred" // => error: reassignment to val
}

{
  case class Company(var name: String)
  val c = Company("Mat-Su Valley Programming")
  c.name
  c.name = "Valley Programming"
}

{
  case class Person(name: String, relation: String)
  val emily = Person("Emily", "niece")

  emily match { case Person(n, r) => println(n, r) }
  val hannah = Person("Hannah", "niece")
  emily == hannah
}

{
  case class Employee(name: String, loc: String, role: String)
  val fred = Employee("Fred", "Anchorage", "Salesman")
  val joe = fred.copy(name = "Joe", role = "Mechanic")
}

// 4.15 Defining an equals Method (Object Equality)

class Person(name: String, age: Int) {
  def canEqual(a: Any) = a.isInstanceOf[Person]
  override def equals(that: Any): Boolean = that match {
    case that: Person => that.canEqual(this) && this.hashCode == that.hashCode
    case _            => false
  }
  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + age;
    result = prime * result + (if (name == null) 0 else name.hashCode)
    result
  }
}

// http://www.scalatest.org/install
import $ivy.`org.scalatest::scalatest:3.1.0`
import org.scalatest.{FunSuite, BeforeAndAfter}

{
  class PersonTests extends FunSuite {
    // these first two instances should be equal
    val nimoy = new Person("Leonard Nimoy", 82)
    val nimoy2 = new Person("Leonard Nimoy", 82)
    val shatner = new Person("William Shatner", 82)
    val ed = new Person("Ed Chigliak", 20)

    // all tests pass
    test("nimoy == nimoy") { assert(nimoy == nimoy) }
    test("nimoy == nimoy2") { assert(nimoy == nimoy2) }
    test("nimoy2 == nimoy") { assert(nimoy2 == nimoy) }
    test("nimoy != shatner") { assert(nimoy != shatner) }
    test("shatner != nimoy") { assert(shatner != nimoy) }
    test("nimoy != null") { assert(nimoy != null) }
    test("nimoy != String") { assert(nimoy != "Leonard Nimoy") }
    test("nimoy != ed") { assert(nimoy != ed) }
  }

  org.scalatest.run(new PersonTests())
}

{
  class Employee(name: String, age: Int, var role: String) extends Person(name, age) {
    override def canEqual(a: Any) = a.isInstanceOf[Employee]
    override def equals(that: Any): Boolean = that match {
      case that: Employee => that.canEqual(this) && this.hashCode == that.hashCode
      case _              => false
    }
    override def hashCode: Int = {
      val ourHash = if (role == null) 0 else role.hashCode
      super.hashCode + ourHash
    }
  }

  class EmployeeTests extends FunSuite with BeforeAndAfter {
    // these first two instance should be equal
    val eNimoy1 = new Employee("Leonard Nimoy", 82, "Actor")
    val eNimoy2 = new Employee("Leonard Nimoy", 82, "Actor")
    val pNimoy = new Person("Leonard Nimoy", 82)
    val eShatner = new Employee("William Shatner", 82, "Actor")
    test("eNimoy1 == eNimoy1") { assert(eNimoy1 == eNimoy1) }
    test("eNimoy1 == eNimoy2") { assert(eNimoy1 == eNimoy2) }
    test("eNimoy2 == eNimoy1") { assert(eNimoy2 == eNimoy1) }
    test("eNimoy != pNimoy") { assert(eNimoy1 != pNimoy) }
    test("pNimoy != eNimoy") { assert(pNimoy != eNimoy1) }
  }

  org.scalatest.run(new PersonTests())
}

// 4.16 Creating Inner Classes
{
  class PandorasBox {
    case class Thing(name: String)

    var things = new collection.mutable.ArrayBuffer[Thing]()
    things += Thing("Evil Thing #1")
    things += Thing("Evil Thing #2")

    def addThing(name: String) { things += new Thing(name) }
  }

  object ClassInAClassExample extends App {
    val p = new PandorasBox
    p.addThing("Evil Thing #3")
    p.addThing("Evil Thing #4")
    p.things.foreach(println)
  }

  ClassInAClassExample.main(Array())
}

{
  class OuterClass {
    class InnerClass {
      var x = 1
    }
  }
  object ClassInObject extends App {
    // inner classes are bound to the object
    val oc1 = new OuterClass
    val oc2 = new OuterClass
    val ic1 = new oc1.InnerClass
    val ic2 = new oc2.InnerClass
    ic1.x = 10
    ic2.x = 20
    println(s"ic1.x = ${ic1.x}")
    println(s"ic2.x = ${ic2.x}")
  }

  ClassInObject.main(Array())
}

{
  object OuterObject {
    class InnerClass {
      var x = 1
    }
  }
  class OuterClass {
    object InnerObject {
      val y = 2
    }
  }

  object InnerClassDemo2 extends App {
    // class inside object
    println(new OuterObject.InnerClass().x)

    // object inside class
    println(new OuterClass().InnerObject.y)
  }

  InnerClassDemo2.main(Array())
}