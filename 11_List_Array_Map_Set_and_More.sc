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
