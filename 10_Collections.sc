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

