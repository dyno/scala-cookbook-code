// 10.1. Understanding the Collections Hierarchy
val v = Vector(1, 2, 3)
v.sum // 6
v.filter(_ > 1) // Vector(2, 3)
v.map(_ * 2) // Vector(2, 4, 6)

val x = IndexedSeq(1, 2, 3) // => Vector(1, 2, 3)
val seq = collection.immutable.LinearSeq(1, 2, 3) // => List(1, 2, 3)
val m = Map(1 -> "a", 2 -> "b")
val m = collection.immutable.Map(1 -> "a", 2 -> "b")
val set = Set(1, 2, 3)
val set = collection.immutable.Set(1, 2, 3)

import scala.collection.mutable
val dl = mutable.DoubleLinkedList(1, 2, 3) // deprecated
val ml = mutable.MutableList(1, 2, 3)

// 10.2. Choosing a Collection Class
// 10.3. Choosing a Collection Method to Solve a Problem
val c = Vector(1, 2, 3, 4, 5, 6)
val c2 = Vector(7, 8, 9, 10, 11, 12)
val primes = Vector(2, 3, 5, 7, 11)

c.collect { case x if primes.contains(x) => x }
c.count(primes.contains)
c.diff(primes)
c.drop(3)
c.dropWhile(_ < 5)
c.exists(primes.contains)
c.filter(primes.contains)
val p: Int => Boolean = x => primes.contains(x)
val p = (x: Int) => primes.contains(x)
c.filterNot(p)
c.find(p)

Vector(c, c2).flatten
Seq(c, c2).flatMap(_.filter(primes.contains))

// def foldLeft[B](initial: B)(op: (B, A) => B): B
def op(total: Int, cur: Int) = total + cur
c.foldLeft(0)(op)
c.foldRight(0)(op)

c.forall(primes.contains)
c.foreach(println)

c.groupBy(primes.contains)
c.hasDefiniteSize
c.head
c.headOption
c.init
c.intersect(primes)
c.isEmpty
c.last
c.lastOption
c.map(primes.contains)
c.max
c.min
c.nonEmpty
c.par
c.partition(primes.contains)
c.product
c.reduceLeft(_ + _)
c.reduceRight(_ + _)
c.reverse
c.size
// c.slice(from, until)
c.slice(2, 4) // 3, 4

c2.zip(c.reverse).sortWith((a, b) => a._2 < b._2)
c2.zip(c.reverse).sortWith(_._2 < _._2)

c.span(_ < 4)

c.splitAt(3)
c.sum
c.tail
c.take(2)
c.takeWhile(!primes.contains(_))
c.union(primes)

c.zip(c.reverse).unzip
c.view
c.zip(c2)
c.zipWithIndex
c.zipWithIndex.map { case (a, b) => (b, a) }

import scala.collection.mutable
val c = mutable.ArrayBuffer[Int]()
val (x, y, z) = (1, 2, 3)
c += x
c += (x, y, z)
c ++ c2
c -= x
c -= (x, y, z)
c --= c2
c ++= c2
c(2) = x
c.remove(2)
c.remove(1, 2)
c.clear

val e = 7
c ++ c2
c :+ e
e +: c
e :: c.toList

val c = mutable.ArrayBuffer[Int]()
c += (x, y, z, e)
c.filter(_ < 5)
c.filterNot(_ < 5)
c.head
c.tail
c.take(2)
c.takeWhile(_ < 5)

val m = Map("a" -> 0, "b" -> 1, "c" -> 2, "d" -> 4, "e" -> 5)
m - "a"
m - ("a", "b", "c")
m -- "abc".split("")

val mm = mutable.Map("a" -> 0, "b" -> 1, "c" -> 2, "d" -> 4, "e" -> 5)
mm += ("f" -> 6)
mm += ("g" -> 7, "h" -> 8)
val c = Seq("i" -> 9, "j" -> 10)
mm ++= c
mm -= "i"
mm -= ("i", "j", "k")
mm --= "ijk".split("")

m.contains(a)
m.filter { case (k, v) => v < 3 }
m.filterKeys(_ > "b")
m.get("a")
m.get("i")
m.getOrElse("a", 3)
m.isDefinedAt("i")
m.keys
m.keysIterator
m.keySet
m.mapValues(_ + 1)
m.values
m.valuesIterator

// 10.4. Understanding the Performance of Collections

// 10.5. Declaring a Type When Creating a Collection
val x = List(1, 2.0, 33D, 400L)
val x = List[Number](1, 2.0, 33D, 400L)
val x = List[AnyVal](1, 2.0, 33D, 400L)

trait Animal
trait FurryAnimal extends Animal
case class Dog(name: String) extends Animal
case class Cat(name: String) extends Animal

val x = Array(Dog("Fido"), Cat("Felix"))
val x = Array[Animal](Dog("Fido"), Cat("Felix"))

class AnimalKingdom {
  def animals = Array(Dog("Fido"), Cat("Felix"))
}
class AnimalKingdom {
  def animals = Array[Animal](Dog("Fido"), Cat("Felix"))
}

// 10.6. Understanding Mutable Variables with Immutable Collections

var sisters = Vector("Melinda")
sisters = sisters :+ "Melissa"
sisters = sisters :+ "Marisa"

var sisters = Vector("Melinda")
sisters = Vector("Melinda", "Melissa")
sisters = Vector("Melinda", "Melissa", "Marisa")


// 10.7. Make Vector Your “Go To” Immutable Sequence
val v = Vector("a", "b", "c")
v(0)
val a = Vector(1, 2, 3)
val b = a ++ Vector(4, 5)
val c = b.updated(0, "x")
val a = Vector(1, 2, 3, 4, 5)
val b = a.take(2)
val c = a.filter(_ > 2)
var a = Vector(1, 2, 3)
a = a ++ Vector(4, 5)

val x = IndexedSeq(1,2,3)

// 10.8. Make ArrayBuffer Your “Go To” Mutable Sequence Problem
import scala.collection.mutable.ArrayBuffer

var fruites = ArrayBuffer[String]()
var ints = ArrayBuffer[Int]()
var nums = ArrayBuffer(1, 2, 3)
nums += 4
nums += (5, 6)
nums ++= List(7, 8)
nums -= 9
nums -= (7, 8)
nums --= Array(5, 6)

val a = ArrayBuffer(1, 2, 3)
a.append(4)
a.append(5, 6)
a.appendAll(Seq(7,8))
a.clear

val a = ArrayBuffer(9, 10)
a.insert(0, 8)
a.insert(0, 6, 7)
a.insertAll(0, Vector(4, 5))
a.prepend(3)
a.prepend(1, 2)
a.prependAll(Array(0))

val a = ArrayBuffer.range('a', 'h')
a.remove(0)
a.remove(2, 3)

val a = ArrayBuffer.range('a', 'h')
a.trimStart(2)
a.trimEnd(2)

// 10.9. Looping over a Collection with foreach
val x = Vector(1, 2, 3)
x.foreach((i: Int) => println(i))

def printIt(c: Char) { println(c) }
"HAL".foreach(c => printIt(c))
"HAL".foreach(printIt)
"HAL".foreach((c: Char) => println(c))

val longWords = new StringBuilder
"Hello world it's Al".split(" ").foreach { e =>
  if (e.length > 4) longWords.append(s" $e")
  else println("Not added: " + e)
}

"Hello world it's Al".split(" ")

val m = Map("fname" -> "Tyler", "lname" -> "LeDude")
m foreach (x => println(s"${x._1} -> ${x._2}"))

movieRatings.foreach {
  case (movie, rating) => println(s"key: $movie, value: $rating")
}

// 10.10. Looping over a Collection with a for Loop
val fruits = Traversable("apple", "banana", "orange")
for (f <- fruits) println(f)
for (f <- fruits) println(f.toUpperCase)

val fruits = Array("apple", "banana", "orange")
for (f <- fruits) {
  // imagine this required multiple lines
  val s = f.toUpperCase
  printlin(s)
}
for (i <- 0 until fruits.size) println(s"element $i is ${fruits(i)}")
for ((elem, count) <- fruits.zipWithIndex) {
  println(s"element $count is $elem")
}
for ((elem, count) <- fruits.view.zipWithIndex) {
  println(s"element $count is $elem")
}
for ((elem, count) <- fruits.zip(Stream from 1)) {
  println(s"element $count is $elem")
}
for (i <- 1 to 3) println(i)
for (i <- 1 to 3) {
  // do whatever you want in this block
  println(i)
}

val fruits = Array("apple", "banana", "orange")
val newArray = for (e <- fruits) yield e.toUpperCase
val newArray = for (fruit <- fruits) yield {
  // imagine this required multiple lines
  val upper = fruit.toUpperCase
  upper
}
{
  val fruits = Array("apple", "banana", "orange")
  def upperReverse(s: String) = {
    // imagine this is a long algorithm
    s.toUpperCase.reverse
  }
  val newArray = for (fruit <- fruits) yield upperReverse(fruit)
}

val names = Map("fname" -> "Ed", "lname" -> "Chigliak")
for ((k, v) <- names) println(s"key: $k, value: $v")

// 10.11. Using zipWithIndex or zip to Create Loop Counters

val days = Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
days.zipWithIndex.foreach {
  case (day, count) => println(s"$count is $day")
}
for ((day, count) <- days.zipWithIndex) {
  println(s"$count is $day")
}
for ((day, count) <- days.zip(Stream from 1)) {
  println(s"day $count is $day")
}

val list = List("a", "b", "c")
val zwi = list.zipWithIndex
val zwi2 = list.view.zipWithIndex

days.zipWithIndex.foreach { d =>
  println(s"${d._2} is ${d._1}")
}

val fruits = Array("apple", "banana", "orange")
for (i <- 0 until fruits.size) println(s"element $i is ${fruits(i)}")

// 10.12. Using Iterators

val it = Iterator(1, 2, 3)
it.foreach(println)
it.foreach(println) // exhausted

// 10.13. Transforming One Collection to Another with for/yield

val a = Array(1, 2, 3, 4, 5)
for (e <- a) yield e
for (e <- a) yield e * 2
for (e <- a) yield e % 2
val fruits = Vector("apple", "banana", "lime", "orange")
val ucFruits = for (e <- fruits) yield e.toUpperCase
for (i <- 0 until fruits.length) yield (i, fruits(i))
for (f <- fruits) yield (f, f.length)
val x = for (e <- fruits) yield {
  // imagine this required multiple lines
  val s = e.toUpperCase
  s
}

case class Person(name: String)
val friends = Vector("Mark", "Regina", "Matt")
for (f <- friends) yield Person(f)
val x = for (e <- fruits if e.length < 6) yield e.toUpperCase
val fruits = scala.collection.mutable.ArrayBuffer("apple", "banana")
val x = for (e <- fruits) yield e.toUpperCase
val fruits = "apple" :: "banana" :: "orange" :: Nil
val x = for (e <- fruits) yield e.toUpperCase

for {
  file <- files
  if hasSoundFileExtension(file)
  if !soundFileIsLong(file)
} yield file

val cars = Vector("Mercedes", "Porsche", "Tesla")
for {
  c <- cars
  if c.startsWith("M")
} yield c

// 10.14. Transforming One Collection to Another with map

val helpers = Vector("adam", "kim", "melissa")
val caps = helpers.map(e => e.capitalize)
val caps = helpers.map(_.capitalize)

val names = Array("Fred", "Joe", "Jonathan")
val lengths = names.map(_.length)
val nieces = List("Aleka", "Christina", "Molly")
val elems = nieces.map(niece => <li>{niece}</li>)
val ul = <ul>{nieces.map(i => <li>{i}</li>)}</ul>

def plusOne(c: Char): Char = (c.toByte + 1).toChar
"HAL".map(plusOne)

val s = " eggs, milk, butter, Coco Puffs "
val items = s.split(",").map(_.trim)

val people = List("adam", "kim", "melissa")
val caps1 = people.map(_.capitalize)
val caps2 = for (p <- people) yield p.capitalize

val fruits = List("apple", "banana", "lime", "orange", "raspberry")
val newFruits = fruits.map(f => if (f.length < 6) f.toUpperCase)
newFruits.filter(_ != ())

val fruits = List("apple", "banana", "lime", "orange", "raspberry")
fruits.filter(_.length < 6).map(_.toUpperCase)

// 10.15. Flattening a List of Lists with flatten

val lol = List(List(1,2), List(3,4))
val result = lol.flatten

val a = Array(Array(1,2), Array(3,4))
a.flatten

val couples = List(List("kim", "al"), List("julia", "terry"))
val people = couples.flatten
val people = couples.flatten.map(_.capitalize).sorted

val myFriends = List("Adam", "David", "Frank")
val adamsFriends = List("Nick K", "Bill M")
val davidsFriends = List("Becca G", "Kenny D", "Bill M")
val friendsOfFriends = List(adamsFriends, davidsFriends)
val uniqueFriendsOfFriends = friendsOfFriends.flatten.distinct

val list = List("Hello", "world")
list.flatten

val x = Vector(Some(1), None, Some(3), None)
x.flatten

// 10.16. Combining map and flatten with flatMap

val bag = List("1", "2", "three", "4", "one hundred seventy five")
def toInt(in: String): Option[Int] = {
  try {
    Some(Integer.parseInt(in.trim))
  } catch {
    case e: Exception => None
  }
}
bag.flatMap(toInt).sum

bag.map(toInt).flatten
bag.map(toInt).flatten.sum
bag.flatMap(toInt).sum

bag.flatMap(toInt).filter(_ > 1)
bag.flatMap(toInt).takeWhile(_ < 4)
bag.flatMap(toInt).partition(_ > 3)

subWords("then")
def subWords(word: String) = List(word, word.tail, word.take(word.length-1))

val words = List("band", "start", "then")
words.map(subWords)
words.map(subWords).flatten
words.flatMap(subWords)

// 10.17. Using filter to Filter a Collection

val x = List.range(1, 10)
val evens = x.filter(_ % 2 == 0)

val fruits = Set("orange", "peach", "apple", "banana")
val x = fruits.filter(_.startsWith("a"))
val y = fruits.filter(_.length > 5)
val list = "apple" :: "banana" :: 1 :: 2 :: Nil
val strings = list.filter {
  case s: String => true
  case _ => false
}

def onlyStrings(a: Any) = a match {
  case s: String => true
  case _ => false
}
val strings = list.filter(onlyStrings)

def getFileContentsWithoutBlanksComments(canonicalFilename: String): List[String] = {
  io.Source
    .fromFile(canonicalFilename)
    .getLines
    .toList
    .filter(_.trim != "")
    .filter(_.charAt(0) != '#')
}

// 10.18. Extracting a Sequence of Elements from a Collection

val x = (1 to 10).toArray
val y = x.drop(3)
val y = x.dropWhile(_ < 6)
val y = x.dropRight(4)
val y = x.take(3)
val y = x.takeWhile(_ < 5)
val y = x.takeRight(3)
val peeps = List("John", "Mary", "Jane", "Fred")
peeps.slice(1, 3)

val nums = (1 to 5).toArray
nums.head
nums.headOption
nums.init
nums.last
nums.lastOption
nums.tail

// 10.19. Splitting Sequences into Subsets (groupBy, partition, etc.)

val x = List(15, 10, 5, 8, 20, 12)
val y = x.groupBy(_ > 10)
val y = x.partition(_ > 10)
val y = x.span(_ < 20)
val y = x.splitAt(2)
val (a,b) = x.partition(_ > 10)
val groups = x.groupBy(_ > 10)
val trues = groups(true)
val falses = groups(false)
val nums = (1 to 5).toArray
nums.sliding(2).toList
nums.sliding(2, 2).toList
nums.sliding(2, 3).toList

val listOfTuple2s = List((1, 2), ('a', 'b'))
val x = listOfTuple2s.unzip
val couples = List(("Kim", "Al"), ("Julia", "Terry"))
val (women, men) = couples.unzip
val women = List("Kim", "Julia")
val men = List("Al", "Terry")
val couples = women zip men

// 10.20. Walking Through a Collection with the reduce and fold Methods

val a = Array(12, 6, 15, 2, 20, 9)
a.reduceLeft(_ + _)
a.reduceLeft((x,y) => x + y)
a.reduceLeft(_ * _)
a.reduceLeft(_ min _)
a.reduceLeft(_ max _)

val findMax = (x: Int, y: Int) => {
  val winner = x max y
  println(s"compared $x to $y, $winner was larger")
  winner
}
a.reduceLeft(findMax)

val peeps = Vector("al", "hannah", "emily", "christina", "aleka")
peeps.reduceLeft((x,y) => if (x.length > y.length) x else y)
peeps.reduceLeft((x,y) => if (x.length < y.length) x else y)

val a = Array(1, 2, 3)
a.reduceLeft(_ + _)
a.foldLeft(20)(_ + _)
a.foldLeft(100)(_ + _)

val divide = (x: Double, y: Double) => {
  val result = x / y
  println(s"divided $x by $y to yield $result")
  result
}
val a = Array(1.0, 2.0, 3.0)
a.reduceLeft(divide)
a.reduceRight(divide)

val product = (x: Int, y: Int) => {
  val result = x * y
  println(s"multiplied $x by $y to yield $result")
  result
}
val a = Array(1, 2, 3)
a.scanLeft(10)(product)

val findMax = (x: Int, y: Int) => {
  Thread.sleep(10)
  val winner = x max y
  println(s"compared $x to $y, $winner was larger")
  winner
}
val a = Array.range(0, 50)
a.par.reduce(findMax)

// 10.21. Extracting Unique Elements from a Sequence

val x = Vector(1, 1, 2, 3, 3, 4)
val y = x.distinct
val s = x.toSet

class Person(firstName: String, lastName: String) {
  override def toString = s"$firstName $lastName"
  def canEqual(a: Any) = a.isInstanceOf[Person]
  override def equals(that: Any): Boolean = that match {
    case that: Person => that.canEqual(this) && this.hashCode == that.hashCode
    case _ => false
  }
  override def hashCode: Int = {
    val prime = 31
    var result = 1
    result = prime * result + lastName.hashCode;
    result = prime * result + (if (firstName == null) 0 else firstName.hashCode)
    return result
  }
}

object Person {
  def apply(firstName: String, lastName: String) =
    new Person(firstName, lastName)
}

val dale1 = new Person("Dale", "Cooper")
val dale2 = new Person("Dale", "Cooper")
val ed = new Person("Ed", "Hurley")
val list = List(dale1, dale2, ed)
val uniques = list.distinct
val list = List(dale1, dale2, ed)
val uniquePeople = list.distinct

// 10.22. Merging Sequential Collections

val a = collection.mutable.ArrayBuffer(1, 2, 3)
a ++= Seq(4, 5, 6)

val a = Array(1, 2, 3)
val b = Array(4, 5, 6)
val c = a ++ b

val a = Array(1, 2, 3, 4, 5)
val b = Array(4, 5, 6, 7, 8)
val c = a.intersect(b)
val c = a.union(b)
val c = a.union(b).distinct
val c = a diff b
val c = b diff a
Array.concat(a, b)
val a = List(1, 2, 3, 4)
val b = List(4, 5, 6, 7)
val c = a ::: b

val a = Array(1, 2, 3, 11, 4, 12, 4, 5)
val b = Array(6, 7, 4, 5)
val c = a.toSet diff b.toSet
val complement = c ++ d

val c = a.toSet -- b.toSet
val d = b.toSet -- a.toSet
val i = a.intersect(b)
val c = a.toSet -- i.toSet
val d = b.toSet -- i.toSet

// 10.23. Merging Two Sequential Collections into Pairs with zip

val women = List("Wilma", "Betty")
val men = List("Fred", "Barney")
val couples = women zip men
for ((wife, husband) <- couples) {
  println(s"$wife is married to $husband")
}
val couplesMap = couples.toMap

val products = Array("breadsticks", "pizza", "soft drink")
val prices = Array(4)
val productsWithPrice = products.zip(prices)
val (a, b) = productsWithPrice.unzip

// 10.24. Creating a Lazy View on a Collection

1 to 100
(1 to 100).view
val view = (1 to 100).view
val x = view.force
(1 to 100).foreach(println)
(1 to 100).view.foreach(println)
(1 to 100).map { _ * 2 }
(1 to 100).view.map { _ * 2 }

val x = (1 to 1000).view.map { e =>
  Thread.sleep(10)
  e * 2
}

val l = List(1, 2, 3)
l.reverse
l.view.reverse

val arr = (1 to 10).toArray
val view = arr.view.slice(2, 5)
arr(2) = 42
view.foreach(println)

view(0) = 10
view(1) = 20
view(2) = 30
arr

// 10.25. Populating a Collection with a Range

Array.range(1, 5)
List.range(0, 10)
Vector.range(0, 10, 2)

val a = (0 until 10).toArray
val list = 1 to 10 by 2 toList
val list = (1 to 10).by(2).toList
val set = Set.range(0, 5)
val set = (0 until 10 by 2).toSet
val letters = ('a' to 'f').toList
val letters = ('a' to 'f').by(2).toList
for (i <- 1 until 10 by 2) println(i)

val map = (1 to 5).map(_ * 2.0)
val map = (1 to 5).map(e => (e, e))
val map = (1 to 5).map(e => (e, e)).toMap

// 10.26. Creating and Using Enumerations

package com.acme.app {
  object Margin extends Enumeration {
    type Margin = Value
    val TOP, BOTTOM, LEFT, RIGHT = Value
  }
}

object Main extends App {
  import com.acme.app.Margin._
  // use an enumeration value in a test
  var currentMargin = TOP

  // later in the code ...
  if (currentMargin == TOP) println("working on Top")

  // print all the enumeration values
  import com.acme.app.Margin
  Margin.values foreach println
}

// a much "heavier" approach
package com.acme.app {
  trait Margin
  case object TOP extends Margin
  case object RIGHT extends Margin
  case object BOTTOM extends Margin
  case object LEFT extends Margin
}

// 10.27. Tuples, for When You Just Need a Bag of Things

val d = ("Debi", 95)
case class Person(name: String)
val t = (3, "Three", new Person("Al"))
t._1
t._2
t._3
val (x, y, z) = (3, "Three", new Person("Al"))
val (x, y, _) = t
val (x, _, _) = t
val (x, _, z) = t
val a = ("AL", "Alabama")
val b = "AL" -> "Alabama"
val c = ("AL" -> "Alabama")
c.getClass
val map = Map("AL" -> "Alabama")

val x = ("AL" -> "Alabama")
val it = x.productIterator
for (e <- it) println(e)
for (e <- it) println(e)

val t = ("AL", "Alabama")
t.productIterator.toArray

// 10.28. Ssorting a Collection

val a = List(10, 5, 8, 1, 7).sorted
val b = List("banana", "pear", "apple", "orange").sorted
List(10, 5, 8, 1, 7).sortWith(_ < _)
List(10, 5, 8, 1, 7).sortWith(_ > _)
List("banana", "pear", "apple", "orange").sortWith(_ < _)
List("banana", "pear", "apple", "orange").sortWith(_ > _)
List("banana", "pear", "apple", "orange").sortWith(_.length < _.length)
List("banana", "pear", "apple", "orange").sortWith(_.length > _.length)
def sortByLength(s1: String, s2: String) = {
  println("comparing %s and %s".format(s1, s2))
  s1.length > s2.length
}
List("banana", "pear", "apple").sortWith(sortByLength)

class Person(var name: String) {
  override def toString = name
}
val ty = new Person("Tyler")
val al = new Person("Al")
val paul = new Person("Paul")
val dudes = List(ty, al, paul)
// dudes.sorted
val sortedDudes = dudes.sortWith(_.name < _.name)
val sortedDudes = dudes.sortWith(_.name > _.name)

class Person(var name: String) extends Ordered[Person] {
  override def toString = name
  // return 0 if the same, negative if this < that, positive if this > that
  // def compare (that: Person) = this.name.compare(that.name)
  def compare(that: Person) = {
    if (this.name == that.name) 0
    else if (this.name > that.name) 1
    else -1
  }
}

val ty = new Person("Tyler")
val al = new Person("Al")
if (al > ty) println("Al") else println("Tyler")

// 10.29. Converting a Collection to a String with mkString

val a = Array("apple", "banana", "cherry")
a.mkString
a.mkString(" ")
a.mkString(", ")
a.mkString("[", ", ", "]")

val a = Array(Array("a", "b"), Array("c", "d"))
a.flatten.mkString(", ")

val v = Vector("apple", "banana", "cherry")
v.toString
