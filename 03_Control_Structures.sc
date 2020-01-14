val x = if (a) y else z

for (line <- source.getLines) {
  for {
    char <- line
    if char.isLetter
  } println(char)
  // char algorithm here ...
}

for {
  line <- source.getLines
  char <- line
  if char.isLetter
} yield char
// char algorithm here ...

val nieces = List("emily", "hannah", "mercedes", "porsche")
for (n <- nieces) yield n.capitalize

// 3.1
val a = Array("apple", "banana", "orange")
for (e <- a) println(e)

for (e <- a) {
  // imagine this requires multiple lines
  val s = e.toUpperCase
  println(s)
}

val newArray = for (e <- a) yield e.toUpperCase

val newArray = for (e <- a) yield {
  // imagine this requires multiple lines
  val s = e.toUpperCase
  s
}

for (i <- 0 until a.length) {
  println(s"$i is ${a(i)}")
}

for ((e, count) <- a.zipWithIndex) {
  println(s"$count is $e")
}

for (i <- 1 to 3) println(i)
1 to 3
for (i <- 1 to 10 if i < 4) println(i)

val names = Map("fname" -> "Robert", "lname" -> "Goren")
for ((k, v) <- names) println(s"key: $k, value: $v")

a.foreach(println)
a.foreach(e => println(e.toUpperCase))
a.foreach { e =>
  val s = e.toUpperCase
  println(s)
}
// $ scalac -Xprint:parse Main.scala
class Main {
  for (i <- 1 to 10) println(i)
}
// $ scalac -Xprint:parse Main.scala

// using ammonite
/*
desugar(for (i <- 1 to 10) println(i))
res133: Desugared = scala.Predef.intWrapper(1).to(10).foreach[Unit](((i: Int) => scala.Predef.println(i)))
 */

val nums = List(1, 2, 3)

for (i <- nums) println(i)
nums.foreach(println)

for (i <- 1 to 10) println(i)
1.to(10).foreach(println)
1 to 10 foreach println

for {
  i <- 1 to 10
  if i % 2 == 0
} println(i)
1.to(10).withFilter(_ % 2 == 0).foreach(println)

for {
  i <- 1 to 10
  if i != 1
  if i % 2 == 0
} println(i)
1.to(10).withFilter(_ != 1).withFilter(_ % 2 == 0).foreach(println)

for { i <- 1 to 10 } yield i
1.to(10).map(identity)

// 3.2
for (i <- 1 to 2; j <- 1 to 2) println(s"i = $i, j = $j")
for {
  i <- 1 to 2
  j <- 1 to 2
} println(s"i = $i, j = $j")

for {
  i <- 1 to 3
  j <- 1 to 5
  k <- 1 to 10
} println(s"i = $i, j = $j, k = $k")

val array = Array.ofDim[Int](2, 2)
array(0)(0) = 0
array(0)(1) = 1
array(1)(0) = 2
array(1)(1) = 3
for {
  i <- 0 to 1
  j <- 0 to 1
} println(s"($i)($j) = ${array(i)(j)}")

for {
  i <- 1 to 2
  j <- 2 to 3
} println(s"i = $i, j = $j")

// 3.3
for (i <- 1 to 10 if i % 2 == 0) println(i)
for {
  i <- 1 to 10
  if i % 2 == 0
} println(i)
for {
  i <- 1 to 10
  if i > 3
  if i < 6
  if i % 2 == 0
} println(i)

for (file <- files) {
  if (hasSoundFileExtension(file) && !soundFileIsLong(file)) {
    soundFiles += file
  }
}
for {
  file <- files
  if passesFilter1(file)
  if passesFilter2(file)
} doSomething(file)

// 3.4
val names = Array("chris", "ed", "maurice")
val capNames = for (e <- names) yield e.capitalize
val lengths = for (e <- names) yield {
  // imagine that this required multiple lines of code
  e.length
}
var fruits = scala.collection.mutable.ArrayBuffer[String]()
fruits += "apple"
fruits += "banana"
fruits += "orange"
val out = for (e <- fruits) yield e.toUpperCase // an ArrayBuffer

val fruits = "apple" :: "banana" :: "orange" :: Nil
val out = for (e <- fruits) yield e.toUpperCase // a List

val out = for (e <- fruits) yield e.toUpperCase
val out = fruits.map(_.toUpperCase)

// 3.5

// package com.alvinalexander.breakandcontinue
import scala.util.control.Breaks._
object BreakAndContinueDemo extends App {
  println("\n=== BREAK EXAMPLE ===")
  breakable {
    for (i <- 1 to 10) {
      println(i)
      if (i > 4) break // break out of the for loop
    }
  }

  println("\n=== CONTINUE EXAMPLE ===")
  val searchMe = "peter piper picked a peck of pickled peppers"
  var numPs = 0
  for (i <- 0 until searchMe.length) {
    breakable {
      if (searchMe.charAt(i) != 'p') {
        break // break out of the 'breakable', continue the outside loop
      } else {
        numPs += 1
      }
    }
  }
  println("Found " + numPs + " p's in the string.")
}

private val breakException = new BreakControl
def break(): Nothing = { throw breakException }
def breakable(op: => Unit) {
  try {
    op
  } catch {
    case ex: BreakControl =>
      if (ex ne breakException) throw ex
  }
}

// break
breakable {
  for (x <- xs) {
    if (cond)
      break
  }
}

// continue
for (x <- xs) {
  breakable {
    if (cond)
      break
  }
}

val count = searchMe.count(_ == 'p')

// package com.alvinalexander.labeledbreaks
object LabeledBreakDemo extends App {
  import scala.util.control._
  val Inner = new Breaks
  val Outer = new Breaks
  Outer.breakable {
    for (i <- 1 to 5) {
      Inner.breakable {
        for (j <- 'a' to 'e') {
          if (i == 1 && j == 'c')
            Inner.break
          else
            println(s"i: $i, j: $j")

          if (i == 2 && j == 'b')
            Outer.break
        }
      }
    }
  }
}

import scala.util.control._
val Exit = new Breaks
Exit.breakable {
  for (j <- 'a' to 'e') {
    if (j == 'c')
      Exit.break
    else
      println(s"j: $j")
  }
}

var barrelIsFull = false
for (monkey <- monkeyCollection if !barrelIsFull) {
  addMonkeyToBarrel(monkey)
  barrelIsFull = checkIfBarrelIsFull
}

def sumToMax(arr: Array[Int], limit: Int): Int = {
  var sum = 0
  for (i <- arr) {
    sum += i
    if (sum > limit)
      return limit
  }
  sum
}
val a = Array.range(0, 10)
println(sumToMax(a, 10))

import scala.annotation.tailrec
def factorial(n: Int): Int = {
  @tailrec
  def factorialAcc(acc: Int, n: Int): Int = {
    if (n <= 1) acc
    else factorialAcc(n * acc, n - 1)
  }

  factorialAcc(1, n)
}

// 3.6
// val absValue = if (a < 0) -a else a
if (i == 0) "a" else "b"
hash = hash * prime + (if (name == null) 0 else name.hashCode)

def abs(x: Int) = if (x >= 0) x else -x
def max(a: Int, b: Int) = if (a > b) a else b
val c = if (a > b) a else b

// 3.7
// i is an integer
i match {
  case 1  => println("January")
  case 2  => println("February")
  case 3  => println("March")
  case 4  => println("April")
  case 5  => println("May")
  case 6  => println("June")
  case 7  => println("July")
  case 8  => println("August")
  case 9  => println("September")
  case 10 => println("October")
  case 11 => println("November")
  case 12 => println("December")
  // catch the default with a variable so you can print it
  case whoa => println("Unexpected case: " + whoa.toString)
}

val month = i match {
  case 1  => "January"
  case 2  => "February"
  case 3  => "March"
  case 4  => "April"
  case 5  => "May"
  case 6  => "June"
  case 7  => "July"
  case 8  => "August"
  case 9  => "September"
  case 10 => "October"
  case 11 => "November"
  case 12 => "December"
  case _  => "Invalid month" // the default, catch-all
}
// The @switch annotation
// Version 1 - compiles to a tableswitch
import scala.annotation.switch
class SwitchDemo {
  val i = 1
  val x = (i: @switch) match {
    case 1 => "One"
    case 2 => "Two"
    case _ => "Other"
  }
}

import scala.annotation.switch
// Version 2 - leads to a compiler warning
class SwitchDemo {
  val i = 1
  val Two = 2 // added
  val x = (i: @switch) match {
    case 1   => "One"
    case Two => "Two" // replaced the '2'
    case _   => "Other"
  }
}

def getClassAsString(x: Any): String = x match {
  case s: String  => s + " is a String"
  case i: Int     => "Int"
  case f: Float   => "Float"
  case l: List[_] => "List"
  case p: Person  => "Person"
  case _          => "Unknown"
}

// case _ => println("Got a default match")
// case default => println(default)
// case oops => println(oops)
i match {
  case 0 => println("0 received")
  case 1 => println("1 is good, too")
}

val monthNumberToName = Map(
  1 -> "January",
  2 -> "February",
  3 -> "March",
  4 -> "April",
  5 -> "May",
  6 -> "June",
  7 -> "July",
  8 -> "August",
  9 -> "September",
  10 -> "October",
  11 -> "November",
  12 -> "December"
)
val monthName = monthNumberToName(4)
println(monthName) // prints "April"

// 3.8
val i = 5 i match {
  case 1 | 3 | 5 | 7 | 9  => println("odd")
  case 2 | 4 | 6 | 8 | 10 => println("even")
}

val cmd = "stop"
cmd match {
  case "start" | "go"           => println("starting")
  case "stop" | "quit" | "exit" => println("stopping")
  case _                        => println("doing nothing")
}

trait Command
case object Start extends Command
case object Go extends Command
case object Stop extends Command
case object Whoa extends Command

def executeCommand(cmd: Command) = cmd match {
  case Start | Go  => start()
  case Stop | Whoa => stop()
}

// 3.9
val evenOrOdd = someNumber match {
  case 1 | 3 | 5 | 7 | 9  => println("odd")
  case 2 | 4 | 6 | 8 | 10 => println("even")
}

def isTrue(a: Any) = a match {
  case 0 | "" => false
  case _      => true
}

// 3.10
i match {
  case 0       => println("1")
  case 1       => println("2")
  case default => println("You gave me: " + default)
}

i match {
  case 0    => println("1")
  case 1    => println("2")
  case whoa => println("You gave me: " + whoa)
}

3 match {
  case 1 => println("one")
  case 2 => println("two")  // no default match
}
// => scala.MatchError

// 3.11

object LargeMatchTest extends App {
  case class Person(firstName: String, lastName: String)
  case class Dog(name: String)

  def echoWhatYouGaveMe(x: Any): String = x match {
    // constant patterns
    case 0       => "zero"
    case true    => "true"
    case "hello" => "you said 'hello'"
    case Nil     => "an empty List"
    // sequence patterns
    // case l: List[Int] => "List" won't work because of type erasure
    case List(0, _, _) => "a three-element list with 0 as the first element"
    case List(1, _*)   => "a list beginning with 1, having any number of elements"
    case Vector(1, _*) =>
      "a vector starting with 1, having any number of elements"
    // tuples
    case (a, b)    => s"got $a and $b"
    case (a, b, c) => s"got $a, $b, and $c"
    // constructor patterns
    case Person(first, "Alexander") =>
      s"found an Alexander, first name = $first"
    case Dog("Suka") => "found a dog named Suka"
    // typed patterns
    case s: String         => s"you gave me this string: $s"
    case i: Int            => s"thanks for the int: $i"
    case f: Float          => s"thanks for the float: $f"
    case a: Array[Int]     => s"an array of int: ${a.mkString(",")}"
    case as: Array[String] => s"an array of strings: ${as.mkString(",")}"
    case d: Dog            => s"dog: ${d.name}"
    case list: List[_]     => s"thanks for the List: $list"
    case m: Map[_, _]      => m.toString
    // the default wildcard pattern
    case _ => "Unknown"
  }

  // trigger the constant patterns
  println(echoWhatYouGaveMe(0))
  println(echoWhatYouGaveMe(true))
  println(echoWhatYouGaveMe("hello"))
  println(echoWhatYouGaveMe(Nil))

  // trigger the sequence patterns
  println(echoWhatYouGaveMe(List(0, 1, 2)))
  println(echoWhatYouGaveMe(List(1, 2)))
  println(echoWhatYouGaveMe(List(1, 2, 3)))
  println(echoWhatYouGaveMe(Vector(1, 2, 3)))

  // trigger the tuple patterns
  // two element tuple
  println(echoWhatYouGaveMe((1, 2)))
  // three element tuple
  println(echoWhatYouGaveMe((1, 2, 3)))

  // trigger the constructor patterns
  println(echoWhatYouGaveMe(Person("Melissa", "Alexander")))
  println(echoWhatYouGaveMe(Dog("Suka")))

  // trigger the typed patterns
  println(echoWhatYouGaveMe("Hello, world"))
  println(echoWhatYouGaveMe(42))
  println(echoWhatYouGaveMe(42F))
  println(echoWhatYouGaveMe(Array(1, 2, 3)))
  println(echoWhatYouGaveMe(Array("coffee", "apple pie")))
  println(echoWhatYouGaveMe(Dog("Fido")))
  println(echoWhatYouGaveMe(List("apple", "banana")))
  println(echoWhatYouGaveMe(Map(1 -> "Al", 2 -> "Alexander")))

  // trigger the wildcard pattern
  println(echoWhatYouGaveMe("33d"))
}

// LargeMatchTest.main(Array())

import java.io.File
sealed trait RandomThing
case class RandomFile(f: File) extends RandomThing
case class RandomString(s: String) extends RandomThing
class RandomNoiseMaker {
  def makeRandomNoise(t: RandomThing) = t match {
    case RandomFile(f)   => playSoundFile(f)
    case RandomString(s) => speak(s)
  }
}

case class Person(firstName: String, lastName: String)
object Test2 extends App {
  def matchType(x: Any): String = x match {
    //case x: List(1, _*) => s"$x" // doesn't compile
    case x @ List(1, _*) => s"$x" // works; prints the list
    //case Some(_) => "got a Some" // works, but can't access the Some
    //case Some(x) => s"$x" // works, returns "foo"
    case x @ Some(_)              => s"$x" // works, returns "Some(foo)"
    case p @ Person(first, "Doe") => s"$p" // works, returns "Person(John,Doe)"
  }

  println(matchType(List(1, 2, 3))) // prints "List(1, 2, 3)"
  println(matchType(Some("foo"))) // prints "Some(foo)"
  println(matchType(Person("John", "Doe"))) // prints "Person(John,Doe)"
}

def toInt(s: String): Option[Int] = {
  try {
    Some(Integer.parseInt(s.trim))
  } catch {
    case e: Exception => None
  }
}

toInt("42") match {
  case Some(i) => println(i)
  case None    => println("That wasn't an Int.")
}

// 3.12
trait Animal
case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal
case object Woodpecker extends Animal
object CaseClassTest extends App {
  def determineType(x: Animal): String = x match {
    case Dog(moniker) => "Got a Dog, name = " + moniker
    case _: Cat       => "Got a Cat (ignoring the name)"
    case Woodpecker   => "That was a Woodpecker"
    case _            => "That was something else"
  }
  println(determineType(new Dog("Rocky")))
  println(determineType(new Cat("Rusty the Cat")))
  println(determineType(Woodpecker))
}

// 3.13
i match {
  case a if 0 to 9 contains a   => println("0-9 range: " + a)
  case b if 10 to 19 contains b => println("10-19 range: " + b)
  case c if 20 to 29 contains c => println("20-29 range: " + c)
  case _                        => println("Hmmm...")
}

num match {
  case x if x == 1             => println("one, a lonely number")
  case x if (x == 2 || x == 3) => println(x)
  case _                       => println("some other value")
}

stock match {
  case x if (x.symbol == "XYZ" && x.price < 20) => buy(x)
  case x if (x.symbol == "XYZ" && x.price > 50) => sell(x)
  case _                                        => // do nothing
}

def speak(p: Person) = p match {
  case Person(name) if name == "Fred"    => println("Yubba dubba doo")
  case Person(name) if name == "Bam Bam" => println("Bam bam!")
  case _                                 => println("Watch the Flintstones!")
}

// 3.14
def isPerson(x: Any): Boolean = x match {
  case p: Person => true
  case _         => false
}

trait SentientBeing
trait Animal extends SentientBeing
case class Dog(name: String) extends Animal
case class Person(name: String, age: Int) extends SentientBeing
// later in the code ...
def printInfo(x: SentientBeing) = x match {
  case Person(name, age) => // handle the Person
  case Dog(name)         => // handle the Dog
}

// 3.15
val x = List(1, 2, 3)
val y = 1 :: 2 :: 3 :: Nil

def listToString(list: List[String]): String = list match {
  case s :: rest => s + " " + listToString(rest)
  case Nil       => ""
}
val fruits = "Apples" :: "Bananas" :: "Oranges" :: Nil
listToString(fruits)

def sum(list: List[Int]): Int = list match {
  case Nil       => 1
  case n :: rest => n + sum(rest)
}
def multiply(list: List[Int]): Int = list match {
  case Nil       => 1
  case n :: rest => n * multiply(rest)
}
val nums = List(1, 2, 3, 4, 5)
sum(nums)
multiply(nums)

// 3.16
val s = "Foo"
try {
  val i = s.toInt
} catch {
  case e: Exception => e.printStackTrace
}

try { openAndReadAFile(filename) } catch {
  case e: FileNotFoundException => println("Couldn't find that file.")
  case e: IOException           => println("Had an IOException trying to read that file")
}

try { openAndReadAFile("foo") } catch {
  case t: Throwable => t.printStackTrace()
}

try {
  val i = s.toInt
} catch {
  case _: Throwable => println("exception ignored")
}

// nothing required here
def toInt(s: String): Option[Int] =
  try {
    Some(s.toInt)
  } catch {
    case e: Exception => throw e
  }

@throws(classOf[NumberFormatException])
def toInt(s: String): Option[Int] =
  try { Some(s.toInt) } catch {
    case e: NumberFormatException => throw e
  }

// 3.17
import java.io._
object CopyBytes extends App {

  // declared before try {}
  var in = None: Option[FileInputStream]
  var out = None: Option[FileOutputStream]

  try {
    in = Some(new FileInputStream("/tmp/Test.class"))
    out = Some(new FileOutputStream("/tmp/Test.class.copy"))
    var c = 0
    while ({ c = in.get.read; c != -1 }) {
      out.get.write(c)
    }
  } catch {
    case e: IOException => e.printStackTrace
  } finally {
    println("entered finally ...")
    if (in.isDefined) in.get.close
    if (out.isDefined) out.get.close
  }
}

try {
  in = Some(new FileInputStream("/tmp/Test.class"))
  out = Some(new FileOutputStream("/tmp/Test.class.copy"))
  in.foreach { inputStream =>
    out.foreach { outputStream =>
      var c = 0
      while ({ c = inputstream.read; c != -1 }) {
        outputStream.write(c)
      }
    }
  }
} catch {
  case e: IOException => e.printStackTrace
} finally {
  println("entered finally ...")
}

// (1) declare the null variables
var store: Store = null
var inbox: Folder = null
try {
  // (2) use the variables/fields in the try block
  store = session.getStore("imaps")
  inbox = getFolder(store, "INBOX")
  // rest of the code here ...
} catch {
  case e: NoSuchProviderException => e.printStackTrace
  case me: MessagingException     => me.printStackTrace
} finally {
  // (3) call close() on the objects in the finally clause
  if (inbox != null) inbox.close
  if (store != null) store.close
}

// 3.18

package com.alvinalexander.controls
import scala.annotation.tailrec
object Whilst {
  @tailrec
  def whilst(testCondition: => Boolean)(codeBlock: => Unit) {
    if (testCondition) {
      codeBlock
      whilst(testCondition)(codeBlock)
    }
  }
}

package foo
import com.alvinalexander.controls.Whilst._
object WhilstDemo extends App {
  var i = 0
  whilst(i < 5) {
    println(i)
    i += 1
  }
}

// two 'if' condition tests
def doubleif(test1: => Boolean)(test2: => Boolean)(codeBlock: => Unit) {
  if (test1 && test2) {
    codeBlock
  }
}
doubleif(age > 18)(numAccidents == 0) { println("Discount!") }
