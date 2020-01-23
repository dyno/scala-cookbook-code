val greater = if (a > b) a else b
val result = try { aString.toInt } catch { case _ => 0 }

// 9.1 Using Function Literals (Anonymous Functions)
val x = List.range(1, 10)
val evens = x.filter((i: Int) => i % 2 == 0)
val evens = x.filter(_ % 2 == 0)
x.foreach((i: Int) => println(i))
x.foreach((i) => println(i))
x.foreach(i => println(i))
x.foreach(println(_))
x.foreach(println)

// 9.2 Using Functions as Variables
(i: Int) => { i * 2 }
val double = (i: Int) => { i * 2 }
double(2) // 4
double(3) // 6
val list = List.range(1, 5)
list.map(double)

val f = (i: Int) => { i % 2 == 0 }
val f: (Int) => Boolean = i => { i % 2 == 0 }
val f: Int => Boolean = i => { i % 2 == 0 }
val f: Int => Boolean = i => i % 2 == 0
val f: Int => Boolean = _ % 2 == 0

// implicit approach
val add = (x: Int, y: Int) => { x + y }
val add = (x: Int, y: Int) => x + y
// explicit approach
val add: (Int, Int) => Int = (x, y) => { x + y }
val add: (Int, Int) => Int = (x, y) => x + y

val addThenDouble: (Int, Int) => Int = (x, y) => {
  val a = x + y
  2 * a
}

def modMethod(i: Int) = i % 2 == 0
def modMethod(i: Int) = { i % 2 == 0 }
def modMethod(i: Int): Boolean = i % 2 == 0
def modMethod(i: Int): Boolean = { i % 2 == 0 }
val list = List.range(1, 10)
list.filter(modMethod)

// partially applied function.
val c = scala.math.cos _
val c = scala.math.cos(_)
c(0)
// Function1[-T1, +R] extends AnyRef

val p = scala.math.pow(_, _)
p(scala.math.E, 2)

def executeFunction(callback:() => Unit) {
  callback()
}

val sayHello = () => { println("Hello") }

def plusOne(i: Int) = i + 1
plusOne(1)
def plusOne(i: Int): Unit = i + 1
plusOne(1)

// 9.3 Defining a Method That Accepts a Simple Function Parameter
{
  def exec(callback: Int => Unit) {
    // invoke the function we were given, giving it an Int parameter
    callback(1)
  }

  val plusOne = (i: Int) => { println(i + 1) }
  exec(plusOne)

  val plusTen = (i: Int) => { println(i + 10) }
  exec(plusTen)
}

{
  val sayHello = () => println("Hello")
  def executeXTimes(callback: () => Unit, numTimes: Int) {
    for (i <- 1 to numTimes) callback()
  }

  executeXTimes(sayHello, 10)
}

{
  def executeAndPrint(f: (Int, Int) => Int, x: Int, y: Int) {
    val result = f(x, y)
    println(result)
  }

  val sum = (x: Int, y: Int) => x + y
  val multiply = (x: Int, y: Int) => x * y

  executeAndPrint(sum, 2, 9) // prints 11
  executeAndPrint(multiply, 3, 9) // prints 27
}

{
  // 1 - define the method
  def exec(callback: (Any, Any) => Unit, x: Any, y: Any) {
    callback(x, y)
  }

  // 2 - define a function to pass in
  val printTwoThings = (a: Any, b: Any) => {
    println(a)
    println(b)
  }

  // 2a - define a method to pass in
  def printTwoThings(a: Any, b: Any) {
    println(a)
    println(b)
  }

  // 3 - pass the function and some parameters to the method
  case class Person(name: String)
  exec(printTwoThings, "Hello", Person("Dave"))
}

{
  // XXX: https://stackoverflow.com/questions/44661257/create-a-class-with-a-package-in-a-scala-ammonite-script
  //package otherscope {
  object otherscope {
    class Foo {
      // a method that takes a function and a string, and passes the string into
      // the function, and then executes the function
      def exec(f: (String) => Unit, name: String) {
        f(name)
      }
    }
  }

  object ClosureExample extends App {
    var hello = "Hello"
    def sayHello(name: String) { println(s"$hello, $name") }

    // execute sayHello from the exec method foo
    val foo = new otherscope.Foo
    foo.exec(sayHello, "Al") // => Hello, Al

    // change the local variable 'hello', then execute sayHello from
    // the exec method of foo, and see what happens
    hello = "Hola"
    foo.exec(sayHello, "Lorenzo") // => Hola, Lorenzo
  }

  ClosureExample.main(Array())
}


val isOfVotingAge = (age: Int) => age >= 18
isOfVotingAge(16) // false
isOfVotingAge(20) // true

{
  var votingAge = 18
  val isOfVotingAge = (age: Int) => age >= votingAge
  isOfVotingAge(16) // false
  isOfVotingAge(20) // true

  def printResult(f: Int => Boolean, x: Int) {
    println(f(x))
  }
  printResult(isOfVotingAge, 20) // true

  // change votingAge in one scope
  votingAge = 21
  // the change to votingAge affects the result
  printResult(isOfVotingAge, 20) // now false
}

{
  import scala.collection.mutable.ArrayBuffer
  val fruits = ArrayBuffer("apple")
  // the function addToBasket has a reference to fruits
  val addToBasket = (s: String) => {
    fruits += s
    println(fruits.mkString(", "))
  }

  def buyStuff(f: String => Unit, s: String) { f(s) }

  buyStuff(addToBasket, "cherries")
  buyStuff(addToBasket, "grapes")
}

// 9.6 Using Partially Applied Functions
val sum = (a: Int, b: Int, c: Int) => a + b + c
val f = sum(1, 2, _: Int)
f(3)

def wrap(prefix: String, html: String, suffix: String) = {
  prefix + html + suffix
}
val wrapWithDiv = wrap("<div>", _: String, "</div>")
wrapWithDiv("<p>Hello, world</p>")
wrapWithDiv("<img src=\"/images/foo.png\" />")
wrap("<pre>", "val x = 1", "</pre>")

// 9.7 Creating a Function That Returns a Function
// function literal, a.k.a anonymous function
(s: String) => { prefix + " " + s }
def saySomething(prefix: String) = (s: String) => { prefix + " " + s }
val sayHello = saySomething("Hello")
sayHello("Al")

def greeting(language: String) = (name: String) => { language match {
case "english" => "Hello, " + name
case "spanish" => "Buenos dias, " + name }
}

def greeting(language: String) = (name: String) => {
  val english = () => "Hello, " + name
  val spanish = () => "Buenos dias, " + name
  language match {
    case "english" => println("returning 'english' function"); english()
    case "spanish" => println("returning 'spanish' function"); spanish()
  }
}
val hello = greeting("english")
val bbuenosDias = greeting("spanish")
hello("Al")
buenosDias("Lorenzo")

// 9.8 Creating Partial Functions
val divide = (x: Int) => 42 / x
divide(0)

val divide = new PartialFunction[Int, Int] {
  def apply(x: Int) = 42 / x
  def isDefinedAt(x: Int) = x != 0
}

divide.isDefinedAt(1)
if (divide.isDefinedAt(1)) divide(1)
divide.isDefinedAt(0)

val divide2: PartialFunction[Int, Int] = {
  case d: Int if d != 0 => 42 / d
}
divide2.isDefinedAt(0)
divide2.isDefinedAt(1)

// converts 1 to "one", etc., up to 5
val convertLowNumToString = new PartialFunction[Int, String] {
  val nums = Array("one", "two", "three", "four", "five")
  def apply(i: Int) = nums(i - 1)
  def isDefinedAt(i: Int) = i > 0 && i < 6
}

for (i <- 1 to 6)
  yield if (convertLowNumToString.isDefinedAt(i)) convertLowNumToString(i) else "N/A"

// converts 1 to "one", etc., up to 5
val convert1to5 = new PartialFunction[Int, String] {
  val nums = Array("one", "two", "three", "four", "five")
  def apply(i: Int) = nums(i - 1)
  def isDefinedAt(i: Int) = i > 0 && i < 6
}
// converts 6 to "six", etc., up to 10
val convert6to10 = new PartialFunction[Int, String] {
  val nums = Array("six", "seven", "eight", "nine", "ten")
  def apply(i: Int) = nums(i - 6)
  def isDefinedAt(i: Int) = i > 5 && i < 11
}
val handle1to10 = convert1to5 orElse convert6to10
handle1to10(3)
handle1to10(8)

val divide: PartialFunction[Int, Int] = {
  case d: Int if d != 0 => 42 / d
}
List(0, 1, 2) map { divide } // => MatchError
List(0, 1, 2) collect { divide } // => List(42, 21)
List(42, "cat") collect { case i: Int => i + 1 }

val sample = 1 to 5
val isEven: PartialFunction[Int, String] = {
  case x if x % 2 == 0 => x + " is even"
}
val evenNumbers = sample collect isEven
val isOdd: PartialFunction[Int, String] = {
  case x if x % 2 == 1 => x + " is odd"
}
val numbers = sample map (isEven orElse isOdd)

// 9.9 A Real-World Example
object NewtonsMethod {
  def main(args: Array[String]) {
    driver
  }

  /**
  * A "driver" function to test Newton's method.
  * Start with (a) the desired f(x) and f'(x) equations,
  * (b) an initial guess and (c) tolerance values.
  */
  def driver {
    // the f(x) and f'(x) functions
    val fx = (x: Double) => 3 * x + math.sin(x) - math.pow(math.E, x)
    val fxPrime = (x: Double) => 3 + math.cos(x) - math.pow(Math.E, x)
    val initialGuess = 0.0
    val tolerance = 0.00005
    // pass f(x) and f'(x) to the Newton's Method function, along with
    // the initial guess and tolerance
    val answer = newtonsMethod(fx, fxPrime, initialGuess, tolerance)

    println(answer)
  }

  /**
  * Newton's Method for solving equations.
  * @todo check that |f(xNext)| is greater than a second tolerance value
  * @todo check that f'(x) != 0
  */
  def newtonsMethod(fx: Double => Double, fxPrime: Double => Double, x: Double, tolerance: Double): Double = {
    var x1 = x
    var xNext = newtonsMethodHelper(fx, fxPrime, x1)
    while (math.abs(xNext - x1) > tolerance) {
      x1 = xNext
      println(xNext) // debugging (intermediate values)
      xNext = newtonsMethodHelper(fx, fxPrime, x1)
    }
    xNext
  }

  /**
  * This is the "x2 = x1 - f(x1)/f'(x1)" calculation
  */
  def newtonsMethodHelper(fx: Double => Double, fxPrime: Double => Double, x: Double): Double = {
    x - fx(x) / fxPrime(x)
  }
}

NewtonsMethod.main(Array())