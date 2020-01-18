// 7.1

package com.acme.store {
  class Foo { override def toString = "I am com.acme.store.Foo" }
}

package com.acme.store
class Foo { override def toString = "I am com.acme.store.Foo" }

package foo.bar.baz
class Foo {
  override def toString = "I'm foo.bar.baz.Foo"
}

// a package containing a class named Foo
package orderentry {
  class Foo { override def toString = "I am orderentry.Foo" }
}

// one package nested inside the other
package customers {
  class Foo { override def toString = "I am customers.Foo" }
  package database {
    // this Foo is different than customers.Foo or orderentry.Foo
    class Foo { override def toString = "I am customers.database.Foo" }
  }
}

// a simple object to test the packages and classes
object PackageTests extends App {
  println(new orderentry.Foo)
  println(new customers.Foo)
  println(new customers.database.Foo)
}

package com.alvinalexander.foo {
  class Foo { override def toString = "I am com.alvinalexander.foo.Foo" }
}

// like java
package foo.bar.baz

class Foo {
  override def toString = "I'm foo.bar.baz.Foo"
}

// 7.2
import java.io.{File, IOException, FileNotFoundException}
import java.io._

package foo
import java.io.File
import java.io.PrintWriter

class Foo {
  import javax.swing.JFrame // only visible in this class
  // ...
}

class Bar {
  import scala.util.Random // only visible in this class
  // ...
}

class Bar {
  def doBar = {
    import scala.util.Random
    println("")
  }
}

// 7.3
import java.util.{ArrayList => JavaList}
val list = new JavaList[String]

import java.util.{Date => JDate, HashMap => JHashMap}
// error: this won't compile because HashMap was renamed
// during the import process
val map = new HashMap[String, String]

import java.util.{HashMap => JavaHashMap}
import scala.collection.mutable.{Map => ScalaMutableMap}
import scala.collection.mutable.Map

import System.out.{println => p}
p("hello")

// 7.4
import java.util.{Random => _, _}
val r = new Random
new ArrayList

import java.util.{List => _, Map => _, Set => _, _}

// 7.5
import java.lang.Math._
val a = sin(0)
val a = cos(PI)
import java.awt.Color._
println(RED)
val currentColor = BLUE

{
  // Java
  package com.alvinalexander.dates;
  public enum Day {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
  }

  import com.alvinalexander.dates.Day._
  // somewhere after the import statement
  if (date == SUNDAY || date == SATURDAY) println("It's the weekend.")

  if (date == Day.SUNDAY || date == Day.SATURDAY) {
    println("It's the weekend.")
  }
}

// 7.6

package foo
import scala.util.Random

class ImportTests {
  def printRandom {
    val r = new Random
  }
}

def getRandomWaitTimeInMinutes: Int = {
  import com.alvinalexander.pandorasbox._
  val p = new Pandora
  p.release
}

def printRandom {
  {
    import scala.util.Random
    val r1 = new Random // this is fine
  }
  val r2 = new Random // error: not found: type Random
}

package orderentry {
  import foo._
  // more code here ...
}
package customers {
  import bar._
  // more code here ...
  package database {
    import baz._
    // more code here ...
  }
}

package foo
// available to all classes defined below
import java.io.File
import java.io.PrintWriter
class Foo {
  // only available inside this class
  import javax.swing.JFrame
  // ...
}
class Bar {
  // only available inside this class
  import scala.util.Random
  // ...
}
