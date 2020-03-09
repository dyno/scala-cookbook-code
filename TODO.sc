/**
* map with sorted keys
*/

// https://alvinalexander.com/scala/how-to-choose-map-implementation-class-sorted-scala-cookbook
import scala.collection.SortedMap
val grades = SortedMap("Kim" -> 90, "Al" -> 85, "Melissa" -> 95, "Emily" -> 91, "Hannah" -> 92)
grades.foreach(println)
for {(k, v) <- grades} yield v -> k

/**
* compare list like python?
*/

// https://stackoverflow.com/questions/11102393/how-to-lexicographically-compare-scala-tuples
val a = Seq(1, 2, 3)
val b = Seq(4, 5)
a.zipAll(b, -1, -1).map { case (a, b) => a.compare(b) }.find(_ != 0)

/**
* try requests and ujson http://www.lihaoyi.com/post/HowtoworkwithHTTPJSONAPIsinScala.html
*/

// https://docs.scala-lang.org/overviews/core/string-interpolation.html
