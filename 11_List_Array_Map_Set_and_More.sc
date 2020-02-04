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

