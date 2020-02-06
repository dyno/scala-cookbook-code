// 12.1. How to Open and Read a Text File

import scala.io.Source
// val filename = "fileopen.scala"
val filename = "12_Files_and_Processes.sc"
for (line <- Source.fromFile(filename).getLines) {
  println(line)
}
val fileContents = Source.fromFile(filename).getLines.mkString

val lines = Source.fromFile(sys.env("HOME") + "/.bash_profile").getLines.toList
val lines = Source.fromFile(sys.env("HOME") + "/.bash_profile").getLines.toArray

val bufferedSource = Source.fromFile("example.txt")
for (line <- bufferedSource.getLines) {
  println(line.toUpperCase)
}
bufferedSource.close

// leaves the file open
for (line <- Source.fromFile("/etc/passwd").getLines) {
  println(line)
}
// also leaves the file open
val contents = Source.fromFile("/etc/passwd").mkString
// sudo lsof -u $USER | grep '/etc/passwd'

// https://stackoverflow.com/questions/20762240/loaner-pattern-in-scala
def using[A](r: Resource)(f: Resource => A): A =
  try {
    f(r)
  } finally {
    r.dispose()
  }

{
  // https://github.com/jsuereth/scala-arm
  import $ivy.`com.jsuereth::scala-arm:2.0`
  import resource._
  object TestARM extends App {
    for (source <- managed(scala.io.Source.fromFile("example.txt"))) {
      for (line <- source.getLines) {
        println(line)
      }
    }
  }

  TestARM.main(Array())
}

{
  // `using` method in the book Beginning Scala (Apress), by David Pollak
  object Control {
    def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
      try {
        f(resource)
      } finally {
        resource.close()
      }
  }

  import Control._
  object TestUsing extends App {
    using(scala.io.Source.fromFile("example.txt")) { source =>
      {
        for (line <- source.getLines) {
          println(line)
        }
      }
    }
  }

  TestUsing.main(Array())
}

{
  // Handling exceptions
  import scala.io.Source
  import java.io.{FileNotFoundException, IOException}
  val filename = "no-such-file.scala"
  try {
    for (line <- Source.fromFile(filename).getLines) {
      println(line)
    }
  } catch {
    case e: FileNotFoundException => println("Couldn't find that file.")
    case e: IOException => println("Got an IOException!")
  }
}

{
  def readTextFile(filename: String): Option[List[String]] = {
    try {
      // `using` is defined above
      val lines = using(scala.io.Source.fromFile(filename)) { source =>
        (for (line <- source.getLines) yield line).toList
      }
      Some(lines)
    } catch {
      case e: Exception => None
    }
  }

  val filename = "/etc/passwd"
  println("--- FOREACH ---")
  val result = readTextFile(filename)
  result foreach { strings =>
    strings.foreach(println)
  }
  println("\n--- MATCH ---")
  readTextFile(filename) match {
    case Some(lines) => lines.foreach(println)
    case None => println("couldn't read file")
  }
}

// specify the encoding
Source.fromFile("example.txt", "UTF-8")

// 12.2. Writing Text Files

val canonicalFilename = "/tmp/hello.txt"
val text = "hello, world"

// PrintWriter
import java.io._
val pw = new PrintWriter(new File(canonicalFilename))
pw.write(text)
pw.close

// FileWriter
val file = new File(canonicalFilename)
val bw = new BufferedWriter(new FileWriter(file))
bw.write(text)
bw.close()
