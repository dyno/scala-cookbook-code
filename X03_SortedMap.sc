/** map with sorted keys */

// https://alvinalexander.com/scala/how-to-choose-map-implementation-class-sorted-scala-cookbook
import scala.collection.SortedMap

val data = Map("Kim" -> 90, "Al" -> 85, "Melissa" -> 95, "Emily" -> 91, "Hannah" -> 92)

// https://stackoverflow.com/questions/15042421/create-sortedmap-from-iterator-in-scala
// val grades = SortedMap(data.toSeq:_*)
val grades = SortedMap[String, Int]() ++ data

grades.foreach(println)

// for yield will produce the same type, i.e. SortedMap
for {(k, v) <- grades} yield v -> k
