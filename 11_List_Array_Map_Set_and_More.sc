// 11.1. Different Ways to Create and Populate a List

val list = 1 :: 2 :: 3 :: Nil
val list = List(1, 2, 3)
val x = List(1, 2.0, 33D, 4000L)
val x = List[Number](1, 2.0, 33D, 4000L)
val x = List.range(1, 10)
val x = List.range(0, 10, 2)
val x = List.fill(3)("foo")
val x = List.tabulate(5)(n => n * n)
val x = collection.mutable.ListBuffer(1, 2, 3).toList
"foo".toList

// 11.2. Creating a Mutable List

import scala.collection.mutable.ListBuffer
var fruits = new ListBuffer[String]()
// add one element at a time to the ListBuffer
fruits += "Apple"
fruits += "Banana"
fruits += "Orange"
// add multiple elements
fruits += ("Strawberry", "Kiwi", "Pineapple")
// remove one element
fruits -= "Apple"
// remove multiple elements
fruits -= ("Banana", "Orange")
// remove multiple elements specified by another sequence
fruits --= Seq("Kiwi", "Pineapple")
// convert the ListBuffer to a List when you need to
val fruitsList = fruits.toList

val x = List(2)
val y = 1 :: x
val z = 0 :: y

// 11.3. Adding Elements to a List

val x = List(2)
val y = 1 :: x
val z = 0 :: y
var x = List(2)
x = 1 :: x
x = 0 :: x
val list1 = 3 :: Nil
val list2 = 2 :: list1
val list3 = 1 :: list2

{
  class Printer {
    def >>(i: Int) { println(s"$i") }
    def >>:(i: Int) { println(s"$i") }
  }
  object RightAssociativeExample extends App {
    val f1 = new Printer
    f1 >> 42
    42 >>: f1
  }

  RightAssociativeExample.main(Array())
}

val x = List(1)
val y = 0 +: x
val y = x :+ 2

// 11.4. Deleting Elements from a List (or ListBuffer)

val originalList = List(5, 1, 4, 3, 2)
val newList = originalList.filter(_ > 2)
var x = List(5, 1, 4, 3, 2)
x = x.filter(_ > 2)

import scala.collection.mutable.ListBuffer
val x = ListBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
x -= 5
x -= (2, 3)
x.remove(0)
x
x.remove(1, 3)
x
val x = ListBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9)
x --= Seq(1, 2, 3)

// 11.5. Merging (Concatenating) Lists

val a = List(1, 2, 3)
val b = List(4, 5, 6)
val c = a ++ b
val c = a ::: b
val c = List.concat(a, b)


// 11.6. Using Stream, a Lazy Version of a List

val stream = 1 #:: 2 #:: 3 #:: Stream.empty
val stream = (1 to 100000000).toStream
stream.head
stream.tail
stream.take(3)
stream.filter(_ < 200)
stream.filter(_ > 200)
stream.map { _ * 2 }

val stream = (1 to 100000000).toStream
stream(0)
stream(1)
// ...
stream(10)

// 11.7. Different Ways to Create and Update an Array

val a = Array(1, 2, 3)
val fruits = Array("Apple", "Banana", "Orange")
val x = Array(1, 2.0, 33D, 400L)
val x = Array[Number](1, 2.0, 33D, 400L)

val fruits = new Array[String](3)
fruits(0) = "Apple"
fruits(1) = "Banana"
fruits(2) = "Orange"

var fruits: Array[String] = _
fruits = Array("apple", "banana")

val x = Array.range(1, 10)
val x = Array.range(0, 10, 2)
val x = Array.fill(3)("foo")
val x = Array.tabulate(5)(n => n * n)
val x = List(1, 2, 3).toArray
"Hello".toArray

val a = Array(1, 2, 3)
a(0)
a(0) = 10
a(1) = 20
a(2) = 30
a

// 11.8. Creating an Array Whose Size Can Change (ArrayBuffer)

import scala.collection.mutable.ArrayBuffer
val characters = ArrayBuffer[String]()
characters += "Ben"
characters += "Jerry"
characters += "Dale"

val characters = ArrayBuffer[String]()
characters += "Dale"
characters += ("Gordon", "Harry")
characters ++= Seq("Andy", "Big Ed")
characters.append("Laura", "Lucy")

// 11.9. Deleting Array and ArrayBuffer Elements Problem

import scala.collection.mutable.ArrayBuffer
val x = ArrayBuffer('a', 'b', 'c', 'd', 'e')
x -= 'a'
x -= ('b', 'c')

val x = ArrayBuffer('a', 'b', 'c', 'd', 'e')
x --= Seq('a', 'b')
x --= Array('c')
x --= Set('d')

val x = ArrayBuffer('a', 'b', 'c', 'd', 'e', 'f')
x.remove(0)
x
x.remove(1, 3)
x

var a = ArrayBuffer(1, 2, 3, 4, 5)
a.clear
a

val a = Array("apple", "banana", "cherry")
a(0) = ""
a(1) = null
a

val a = Array("apple", "banana", "cherry")
val b = a.filter(!_.contains("apple"))
var a = Array("apple", "banana", "cherry")
a = a.take(2)

// 11.10. Sorting Arrays

val fruits = Array("cherry", "apple", "banana")
scala.util.Sorting.quickSort(fruits)
fruits

// 11.11. Creating Multidimensional Arrays

val rows = 2
val cols = 3
val a = Array.ofDim[String](rows, cols)
a(0)(0) = "a"
a(0)(1) = "b"
a(0)(2) = "c"
a(1)(0) = "d"
a(1)(1) = "e"
a(1)(2) = "f"
val x = a(0)(0)

for {
  i <- 0 until rows
  j <- 0 until cols
} println(s"($i)($j) = ${a(i)(j)}")

val x, y, z = 10
val a = Array.ofDim[Int](x, y, z)
for {
  i <- 0 until x
  j <- 0 until y
  k <- 0 until z
} println(s"($i)($j)($k) = ${a(i)(j)(k)}")

val a = Array(Array("a", "b", "c"), Array("d", "e", "f"))
a(0)
a(0)(0)

val a = Array(Array("a", "b", "c"), Array("d", "e"))
var arr = Array(Array("a", "b", "c"))
arr ++= Array(Array("d", "e"))
arr

class Test {
  val arr = Array.ofDim[String](2, 3)
}

// 11.12. Creating Maps

val states = Map("AL" -> "Alabama", "AK" -> "Alaska")
var states = collection.mutable.Map("AL" -> "Alabama")
var states = collection.mutable.Map[String, String]()
states += ("AL" -> "Alabama")
val states = Map( ("AL", "Alabama"), ("AK", "Alaska") )

import scala.collection.mutable.{Map => MMap}
object Test extends App {
  // MMap is really scala.collection.mutable.Map
  val m = MMap(1 -> 'a')
  for ((k, v) <- m) println(s"$k, $v")
}
Test.main(Array())

// 11.13. Choosing a Map Implementation

import scala.collection.SortedMap
val grades = SortedMap("Kim" -> 90, "Al" -> 85, "Melissa" -> 95, "Emily" -> 91, "Hannah" -> 92)

import scala.collection.mutable.LinkedHashMap
var states = LinkedHashMap("IL" -> "Illinois")
states += ("KY" -> "Kentucky")
states += ("TX" -> "Texas")

import scala.collection.mutable.ListMap
var states = ListMap("IL" -> "Illinois")
states += ("KY" -> "Kentucky")
states += ("TX" -> "Texas")

import collection.immutable.Map
import collection.mutable.Map
import collection.mutable.LinkedHashMap
import collection.immutable.ListMap
import collection.mutable.ListMap
import collection.SortedMap

import collection.immutable.HashMap
import collection.mutable.ObservableMap
import collection.mutable.MultiMap
import collection.mutable.SynchronizedMap
import collection.immutable.TreeMap
import collection.mutable.WeakHashMap

import collection.parallel.immutable.ParHashMap
import collection.parallel.mutable.ParHashMap
import collection.concurrent.TrieMap

// 11.14. Adding, Updating, and Removing Elements with a Mutable Map

var states = scala.collection.mutable.Map[String, String]()
states("AK") = "Alaska"
states += ("AL" -> "Alabama")
states += ("AR" -> "Arkansas", "AZ" -> "Arizona")
states ++= List("CA" -> "California", "CO" -> "Colorado")
states -= "AR"
states -= ("AL", "AZ")
states --= List("AL", "AZ")
states("AK") = "Alaska, A Really Big State"
states

val states = collection.mutable.Map("AK" -> "Alaska", "IL" -> "Illinois", "KY" -> "Kentucky")
states.put("CO", "Colorado")
states.retain((k, v) => k == "AK")
states.remove("AK")
states
states.clear
states

// 11.15. Adding, Updating, and Removing Elements with Immutable Maps

val a = Map("AL" -> "Alabama")
val b = a + ("AK" -> "Alaska")
val c = b + ("AR" -> "Arkansas", "AZ" -> "Arizona")
val d = c + ("AR" -> "banana")
val e = d - "AR"
val f = e - "AZ" - "AL"

var x = Map("AL" -> "Alabama")
x += ("AK" -> "Alaska")
x += ("AR" -> "Arkansas", "AZ" -> "Arizona")
x += ("AR" -> "banana")
x -= "AR"
x = Map("CO" -> "Colorado")
x("AL") = "foo"; x

// 11.16. Accessing Map Values

val states = Map("AL" -> "Alabama", "AK" -> "Alaska", "AZ" -> "Arizona")
val az = states("AZ")
val s = states("FOO")
val states = Map("AL" -> "Alabama").withDefaultValue("Not found")
val s = states.getOrElse("FOO", "No such state")
val az = states.get("AZ")
val za = states.get("FOO")

// 11.17. Traversing a Map

val ratings = Map("Lady in the Water" -> 3.0, "Snakes on a Plane" -> 4.0, "You, Me and Dupree" -> 3.5)
for ((k, v) <- ratings) println(s"key: $k, value: $v")

ratings.foreach {
  case (movie, rating) => println(s"key: $movie, value: $rating")
}

ratings.foreach(x => println(s"key: ${x._1}, value: ${x._2}"))
ratings.keys.foreach((movie) => println(movie))
ratings.keys.foreach(println)
ratings.values.foreach((rating) => println(rating))
var x = collection.mutable.Map(1 -> "a", 2 -> "b")
val y = x.mapValues(_.toUpperCase)
val map = Map(1 -> 10, 2 -> 20, 3 -> 30)
val newMap = map.transform((k, v) => k + v)

// 11.18. Getting the Keys or Values from a Map

val states = Map("AK" -> "Alaska", "AL" -> "Alabama", "AR" -> "Arkansas")
states.keySet
states.keys
states.keysIterator
states.values
states.valuesIterator

// 11.19. Reversing Keys and Values

val map = Map(1 -> 10, 2 -> 20, 3 -> 30)
val reverseMap = for ((k,v) <- map) yield (v, k)
val products = Map("Breadsticks" -> "$5", "Pizza" -> "$10", "Wings" -> "$5")
val reverseMap = for ((k,v) <- products) yield (v, k)

// 11.20. Testing for the Existence of a Key or Value in a Map

val states = Map("AK" -> "Alaska", "IL" -> "Illinois", "KY" -> "Kentucky")
if (states.contains("FOO")) println("Found foo") else println("No foo")
states.valuesIterator.exists(_.contains("ucky"))
states.valuesIterator.exists(_.contains("yucky"))
states.valuesIterator

// 11.21. Filtering a Map

var x = collection.mutable.Map(1 -> "a", 2 -> "b", 3 -> "c")
x.retain((k, v) => k > 1)
x

x.transform((k,v) => v.toUpperCase)
x

val x = Map(1 -> "a", 2 -> "b", 3 -> "c")
val y = x.filterKeys(_ > 2)

def only1(i: Int) = if (i == 1) true else false
val x = Map(1 -> "a", 2 -> "b", 3 -> "c")
val y = x.filterKeys(only1)

var m = Map(1 -> "a", 2 -> "b", 3 -> "c")
val newMap = m.filterKeys(Set(2, 3))
var m = Map(1 -> "a", 2 -> "b", 3 -> "c")
m.filter((t) => t._1 > 1)
m.filter((t) => t._2 == "c")
m.take(2)

// 11.22. Sorting an Existing Map by Key or Value

val grades = Map("Kim" -> 90, "Al" -> 85, "Melissa" -> 95, "Emily" -> 91, "Hannah" -> 92)
import scala.collection.immutable.ListMap
ListMap(grades.toSeq.sortBy(_._1): _*)
ListMap(grades.toSeq.sortWith(_._1 < _._1): _*)
ListMap(grades.toSeq.sortWith(_._1 > _._1): _*)
ListMap(grades.toSeq.sortBy(_._2): _*)
ListMap(grades.toSeq.sortWith(_._2 < _._2): _*)
ListMap(grades.toSeq.sortWith(_._2 > _._2): _*)
val x = collection.mutable.LinkedHashMap(grades.toSeq.sortBy(_._1): _*)
x.foreach(println)
val grades = Map("Kim" -> 90, "Al" -> 85, "Melissa" -> 95, "Emily" -> 91, "Hannah" -> 92)

val x = grades.toSeq.sortBy(_._1)
ListMap(x: _*)

def printAll(strings: String*) {
  strings.foreach(println)
}
val fruits = List("apple", "banana", "cherry")
printAll(fruits: _*)

// 11.23. Finding the Largest Key or Value in a Map

val grades = Map("Al" -> 80, "Kim" -> 95, "Teri" -> 85, "Julia" -> 90)
grades.max
grades.keysIterator.max
grades.keysIterator.reduceLeft((x, y) => if (x > y) x else y)
grades.keysIterator.reduceLeft((x, y) => if (x.length > y.length) x else y)
grades.valuesIterator.max
grades.valuesIterator.reduceLeft(_ max _)
grades.valuesIterator.reduceLeft((x, y) => if (x > y) x else y)

// 11.24. Adding Elements to a Set

var set = scala.collection.mutable.Set[Int]()
set += 1
set += (2, 3)
set += 2
set ++= Vector(4, 5)
set.add(6)
set.add(5)
set.contains(5)
var set = scala.collection.mutable.Set(1, 2, 3)
val s1 = Set(1, 2)
val s2 = s1 + 3
val s3 = s2 + (4, 5)
val s4 = s3 ++ List(6, 7)
var set = Set(1, 2, 3)
set += 4
set

// 11.25. Deleting Elements from Sets

var set = scala.collection.mutable.Set(1, 2, 3, 4, 5)
set -= 1
set -= (2, 3)
set --= Array(4, 5)
var set = scala.collection.mutable.Set(1, 2, 3, 4, 5)
set.retain(_ > 2)
set
var set = scala.collection.mutable.Set(1, 2, 3, 4, 5)
set.clear
var set = scala.collection.mutable.Set(1, 2, 3, 4, 5)
set.remove(2)
set
set.remove(40)

val s1 = Set(1, 2, 3, 4, 5, 6)
val s2 = s1 - 1
val s3 = s2 - (2, 3)
val s4 = s3 -- Array(4, 5)
val s1 = Set(1, 2, 3, 4, 5, 6)
val s2 = s1.filter(_ > 3)
val firstTwo = s1.take(2)

// 11.26. Using Sortable Sets

val s = scala.collection.SortedSet(10, 4, 8, 2)
val s = scala.collection.SortedSet("cherry", "kiwi", "apple")
var s = scala.collection.mutable.LinkedHashSet(10, 4, 8, 2)

{
  class Person(var name: String)
  import scala.collection.SortedSet
  val aleka = new Person("Aleka")
  val christina = new Person("Christina")
  val molly = new Person("Molly")
  val tyler = new Person("Tyler")

  // this won't work
  val s = SortedSet(molly, tyler, christina, aleka)
}

{
  class Person(var name: String) extends Ordered[Person] {
    override def toString = name

    // return 0 if the same, negative if this < that, positive if this > that
    def compare(that: Person) = {
      if (this.name == that.name) 0
      else if (this.name > that.name) 1
      else -1
    }
  }

  val aleka = new Person("Aleka")
  val christina = new Person("Christina")
  val molly = new Person("Molly")
  val tyler = new Person("Tyler")

  val s = SortedSet(molly, tyler, christina, aleka)
}

// 11.27. Using a Queue

import scala.collection.mutable.Queue
var ints = Queue[Int]()
var fruits = Queue[String]()
var q = Queue[Person]()

val q = Queue(1, 2, 3)
import scala.collection.mutable.Queue
var q = new Queue[String]
q += "apple"
q += ("kiwi", "banana")
q ++= List("cherry", "coconut")
q.enqueue("pineapple")
val next = q.dequeue
q
val next = q.dequeue
q
q.dequeueFirst(_.startsWith("b"))
q
q.dequeueAll(_.length > 6)
q

// 11.28. Using a Stack

import scala.collection.mutable.Stack
var ints = Stack[Int]()
var fruits = Stack[String]()
case class Person(var name: String)
var people = Stack[Person]()
val ints = Stack(1, 2, 3)

var fruits = Stack[String]()
fruits.push("apple")
fruits.push("banana")
fruits.push("coconut", "orange", "pineapple")
val next = fruits.pop
fruits
fruits.top
fruits
fruits.size
fruits.isEmpty
fruits.clear
fruits

// 11.29. Using a Range

1 to 10
1 until 10
1 to 10 by 2
'a' to 'c'
val x = (1 to 10).toList
val x = (1 to 10).toArray
val x = (1 to 10).toSet
val x = Array.range(1, 10)
val x = Vector.range(1, 10)
val x = List.range(1, 10)
val x = List.range(0, 10, 2)
val x = collection.mutable.ArrayBuffer.range('a', 'd')
for (i <- 1 to 3) println(i)

val x = (1 to 5).map { e =>
  (e + 1.1) * 2
}
val x = List.tabulate(5)(_ + 1)
val x = List.tabulate(5)(_ + 2)
val x = Vector.tabulate(5)(_ * 2)

List(1, 2, 3, 4).sliding(2, 2).map { case List(k, v) => k -> v} toMap
List(1, 2, 3, 4).grouped(2).map { case List(k, v) => k -> v} toMap
