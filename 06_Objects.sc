// 6.1
{
  val a = 10
  val b = a.asInstanceOf[Long]
  val c = a.asInstanceOf[Byte]
  val objects = Array("a", 1)
  val arrayOfObject = objects.asInstanceOf[Array[Object]]

  val i = 1
  // i.asInstanceOf[String]
}

// 6.2

val info = new DataLine.Info(classOf[TargetDataLine], null)

// java
// info = new DataLine.Info(TargetDataLine.class, null);
val stringClass = classOf[String]
stringClass.getMethods

// 6.3

{
  def printAll(numbers: Int*) { println("class: " + numbers.getClass) }
  printAll(1, 2, 3)
  printAll()

  import $ivy.`org.scala-lang.modules::scala-xml:1.2.0`
  val hello: scala.xml.Elem = <p>Hello, world</p>
  hello.child.foreach(e => println(e.getClass))
  val hello: scala.xml.Elem = <p>Hello, <br/>world</p>
  hello.child.foreach(e => println(e.getClass))

  def printClass(c: Any) { println(c.getClass) }
  printClass(1)
  printClass("yo")
}

// 6.4

{
  object Hello extends App {
    println("Hello, world")
  }

  Hello.main(Array())
}

{
  object Hello extends App {
    if (args.length == 1)
      println(s"Hello, ${args(0)}")
    else
      println("I didn't get your name.")
  }

  Hello.main(Array("World"))
}

{
  object Hello2 {
    def main(args: Array[String]) {
      println("Hello, world")
    }
  }

  Hello2.main(Array())
}

// 6.5
{
  object CashRegister {
    def open { println("opened") }
    def close { println("closed") }
  }

  object Main extends App {
    CashRegister.open
    CashRegister.close
  }
}

{
  import java.util.Calendar
  import java.text.SimpleDateFormat

  object DateUtils {
    // as "Thursday, November 29"
    def getCurrentDate: String = getCurrentDateTime("EEEE, MMMM d")
    // as "6:20 p.m."
    def getCurrentTime: String = getCurrentDateTime("K:m aa")

    // a common function used by other date/time functions
    private def getCurrentDateTime(dateTimeFormat: String): String = {
      val dateFormat = new SimpleDateFormat(dateTimeFormat)
      val cal = Calendar.getInstance()
      dateFormat.format(cal.getTime())
    }
  }

  DateUtils.getCurrentDate
  DateUtils.getCurrentTime
}

// 6.6

{
  // Pizza class
  class Pizza(var crustType: String) {
    override def toString = "Crust type is " + crustType
  }
  // companion object
  object Pizza {
    val CRUST_TYPE_THIN = "thin"
    val CRUST_TYPE_THICK = "thick"
    def getFoo = "Foo"
  }

  println(Pizza.CRUST_TYPE_THIN)
  println(Pizza.getFoo)
  var p = new Pizza(Pizza.CRUST_TYPE_THICK)
  println(p)
}

{
  class Foo {
    private val secret = 2
  }
  object Foo {
    // access the private class field 'secret'
    def double(foo: Foo) = foo.secret * 2
  }
  object Driver extends App {
    val f = new Foo println (Foo.double(f)) // prints 4
  }

  Driver.main(Array())
}

{
  class Foo {
    // access the private object field 'obj'
    def printObj { println(s"I can see ${Foo.obj}") }
  }
  object Foo {
    private val obj = "Foo's object"
  }
  object Driver extends App {
    val f = new Foo
    f.printObj
  }

  Driver.main(Array())
}

// 6.7
package com.alvinalexander.myapp
// com/alvinalexander/myapp/package.scala
package object model {
  // field
  val MAGIC_NUM = 42

  // method
  def echo(a: Any) { println(a) }

  // enumeration
  object Margin extends Enumeration {
    type Margin = Value
    val TOP, BOTTOM, LEFT, RIGHT = Value
  }

  // type definition
  type MutableMap[K, V] = scala.collection.mutable.Map[K, V]
  val MutableMap = scala.collection.mutable.Map

  // ---------------------------------------------------------------------------
  object MainDriver extends App {
    // access our method, constant, and enumeration
    echo("Hello, world")
    echo(MAGIC_NUM)
    echo(Margin.LEFT)
    // use our MutableMap type (scala.collection.mutable.Map)
    val mm = MutableMap("name" -> "Al")
    mm += ("password" -> "123")
    for ((k, v) <- mm) printf("key: %s, value: %s\n", k, v)
  }

  MainDriver.main(Array())
}

// 6.8
{
  class Person {
    var name: String = _
  }

  object Person {
    def apply(name: String): Person = {
      var p = new Person
      p.name = name
      p
    }
  }

  val dawn = Person("Dawn")
  val a = Array(Person("Dan"), Person("Elijah"))
}

{
  case class Person(var name: String)
  val p = Person("Fred Flinstone")
  val p = Person.apply("Fred Flinstone")
}

{
  class Person {
    var name = ""
    var age = 0
  }

  object Person {
    // a one-arg constructor
    def apply(name: String): Person = {
      var p = new Person
      p.name = name
      p
    }

    // a two-arg constructor
    def apply(name: String, age: Int): Person = {
      var p = new Person
      p.name = name
      p.age = age
      p
    }
  }

  val fred = Person("Fred")
  val john = Person("John", 42)
}

{
  // want accessor and mutator methods for the name and age fields
  case class Person(var name: String, var age: Int)

  // define two auxiliary constructors
  object Person {
    def apply() = new Person("<no name>", 0)
    def apply(name: String) = new Person(name, 0)
  }

  object Test extends App {
    val a = Person()
    val b = Person("Al")
    val c = Person("William Shatner", 82)
    println(a)
    println(b)
    println(c)
    // test the mutator methods
    a.name = "Leonard Nimoy"
    a.age = 82
    println(a)
  }

  Test.main(Array())
}

// 6.9
{
  trait Animal {
    def speak
  }

  object Animal {
    private class Dog extends Animal {
      override def speak { println("woof") }
    }
    private class Cat extends Animal {
      override def speak { println("meow") }
    }

    // the factory method
    def apply(s: String): Animal = {
      if (s == "dog") new Dog
      else new Cat
    }
  }

  val cat = Animal("cat") // returns a Cat
  val dog = Animal("dog") // returns a Dog
}
