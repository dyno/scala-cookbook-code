/** compare list like python */

// https://stackoverflow.com/questions/11102393/how-to-lexicographically-compare-scala-tuples
val a = (1, 2, 3)
val b = (4, 5)
val r = a.zipAll(b, -1, -1).map { case (a, b) => a.compare(b) }.find(_ != 0)

