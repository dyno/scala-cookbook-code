"Hello, world".getClass.getName
val s = "Hello, world"
s.length
val s = "Hello" + " world"
"hello".foreach(println)
for (c <- "hello") println(c)
s.getBytes.foreach(println)
val result = "hello world".filter(_ != 'l')
"scala".drop(2).take(2).capitalize

// 1.1
val s1 = "Hello"
val s2 = "Hello"
val s3 = "H" + "ello"
s1 == s2
s1 == s3
val s4: String = null
s3 == s4
s4 == s3

val s1 = "Hello"
val s2 = "hello"
s1.toUpperCase == s2.toUpperCase

val s1: String = null
val s2: String = null
s1.toUpperCase == s2.toUpperCase

val a = "Marisa"
val b = "marisa"
a.equalsIgnoreCase(b)

// 1.2
val foo = """This is
a multiline
String"""

val speech = """Four score and
  |seven years ago""".stripMargin
val speech = """Four score and
  #seven years ago""".stripMargin('#')

val speech = """Four score and
|seven years ago
|our fathers""".stripMargin.replaceAll("\n", " ")

val s = """This is known as a
|"multiline" string
|or 'heredoc' syntax.""".stripMargin.replaceAll("\n", " ")

// 1.3
"hello world".split(" ")
"hello world".split(" ").foreach(println)
val s = "eggs, milk, butter, Coco Puffs"
s.split(",")
s.split(",").map(_.trim)
"hello world, this is Al".split("\\s+")
"hello world".split(" ")
"hello world".split(' ')
// 1.4
val name = "Fred"
val age = 33
val weight = 200.00
println(s"$name is $age years old, and weighs $weight pounds.")
println(s"Age next year: ${age + 1}")
println(s"You are 33 years old: ${age == 33}")
case class Student(name: String, score: Int)
val hannah = Student("Hannah", 95)
println(s"${hannah.name} has a score of ${hannah.score}")
println(s"$hannah.name has a score of $hannah.score")
println(f"$name is $age years old, and weighs $weight%.2f pounds.")
println(f"$name is $age years old, and weighs $weight%.0f pounds.")
val out = f"$name, you weigh $weight%.0f pounds."

s"foo\nbar"
raw"foo\nbar"

val name = "Fred"
val age = 33
val s = "%s is %d years old".format(name, age)
println("%s is %d years old".format(name, age))
override def toString: String = "%s %s, age %d".format(firstName, lastName, age)

// 1.5
val upper = "hello, world".map(c => c.toUpper)
val upper = "hello, world".map(_.toUpper)
val upper = "hello, world".filter(_ != 'l').map(_.toUpper)
for (c <- "hello") println(c)
val upper = for (c <- "hello, world") yield c.toUpper
val result = for {
  c <- "hello, world" if c != 'l'
} yield c.toUpper
"hello".foreach(println)
String s = "Hello";

/*
 * StringBuilder sb = new StringBuilder( ;
 * for (int i = 0; i < s.length(); i++) {
 *   char c = s.charAt(i);
 *   // do something with the character ...
 *   // sb.append ...
 * }
 * String result = sb.toString();
 */
"HELLO".map(c => (c.toByte + 32).toChar)
"HELLO".map { c =>
  (c.toByte + 32).toChar
}
def toLower(c: Char): Char = (c.toByte + 32).toChar
"HELLO".map(toLower)
val s = "HELLO"
for (c <- s) yield toLower(c)

package tests
/**
* Calculate the Adler-32 checksum using Scala.
* @see http://en.wikipedia.org/wiki/Adler-32
*/

object Adler32Checksum {
  val MOD_ADLER = 65521
  def main(args: Array[String]) {
    val sum = adler32sum("Wikipedia")
    printf("checksum (int) = %d\n", sum)
    printf("checksum (hex) = %s\n", sum.toHexString)
  }

  def adler32sum(s: String): Int = {
    var a = 1
    var b = 0
    s.getBytes.foreach { char =>
      a = (char + a) % MOD_ADLER
      b = (b + a) % MOD_ADLER
    }
    // note: Int is 32 bits, which this requires
    b * 65536 + a // or (b << 16) + a }
  }
}

"hello".getBytes
"hello".getBytes.foreach(println)

// 1.6
val numPattern = "[0-9]+".r
val address = "123 Main Street Suite 101"
val match1 = numPattern.findFirstIn(address)
val match2 = numPattern.findAllIn(address)
match2.foreach(println)

import scala.util.matching.Regex
val numPattern = new Regex("[0-9]+")
val address = "123 Main Street Suite 101"
val match1 = numPattern.findFirstIn(address)
match1 match {
  case Some(s) => println(s"Found: $s")
  case None    =>
}

// 1.7
val address = "123 Main Street".replaceAll("[0-9]", "x")
val regex = "[0-9]".r
val newAddress = regex.replaceAllIn("123 Main Street", "x")
val result = "123".replaceFirst("[0-9]", "x")
val regex = "H".r
val result = regex.replaceFirstIn("Hello world", "J")

// 1.8
val pattern = "([0-9]+) ([A-Za-z]+)".r
val pattern(count, fruit) = "100 Bananas"

// match "movies 80301"
val MoviesZipRE = "movies (\\d{5})".r
// match "movies near boulder, co"
val MoviesNearCityStateRE = "movies near ([a-z]+), ([a-z]{2})".r
textUserTyped match {
  case MoviesZipRE(zip)                   => getSearchResults(zip)
  case MoviesNearCityStateRE(city, state) => getSearchResults(city, state)
  case _                                  => println("did not match a regex")
}

// 1.9
"hello".charAt(0)
"hello" (0)
"hello" (1)
"hello".apply(1)

// 1.10
implicit class StringImprovements(s: String) {
  def increment = s.map(c => (c + 1).toChar)
}

"HAL".increment

package com.alvinalexander.utils

object StringUtils {
  implicit class StringImprovements(val s: String) {
    def increment = s.map(c => (c + 1).toChar)
  }
}

package foo.bar

import com.alvinalexander.utils.StringUtils._

object Main extends App {
  println("HAL".increment)
}

// package.scala
package com.alvinalexander
package object utils {
  implicit class StringImprovements(val s: String) {
    def increment = s.map(c => (c + 1).toChar)
  }
}

package foo.bar
import com.alvinalexander.utils._

object MainDriver extends App {
  println("HAL".increment)
}

implicit class StringImprovements(val s: String) {
  // being explicit that each method returns a String
  def increment: String = s.map(c => (c + 1).toChar)
  def decrement: String = s.map(c => (c − 1).toChar)
  def hideAll: String = s.replaceAll(".", "*")
}

implicit class StringImprovements(val s: String) {
  def increment = s.map(c => (c + 1).toChar)
  def decrement = s.map(c => (c - 1).toChar)
  def hideAll: String = s.replaceAll(".", "*")
  def plusOne = s.toInt + 1
  def asBoolean = s match {
    case "0" | "zero" | "" | " " => false
    case _                       => true
  }
}
