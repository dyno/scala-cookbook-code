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
