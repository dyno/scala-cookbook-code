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

// 12.3. Reading and Writing Binary Files

import java.io._
object CopyBytes extends App {
  var in: Option[FileInputStream] = None
  var out: Option[FileOutputStream] = None
  try {
    in = Some(new FileInputStream("/etc/hosts"))
    out = Some(new FileOutputStream("/tmp/hosts.copy"))
    var c = 0
    while ({ c = in.get.read; c != -1 }) {
      out.get.write(c)
    }
  } catch {
    case e: IOException => e.printStackTrace
  } finally {
    println("entered finally ...")
    if (in.isDefined) in.get.close
    if (out.isDefined) out.get.close
  }
}
CopyBytes.main(Array())

// 12.4 How to Process Every Character in a Text File

val source = scala.io.Source.fromFile("/etc/hosts")
for (char <- source) {
  println(char.toUpper)
}
source.close

// run time: took 100 secs
def countLines1(source: io.Source): Long = {
  val NEWLINE = 10
  var newlineCount = 0L
  for {
    char <- source
    if char.toByte == NEWLINE
  } newlineCount += 1

  newlineCount
}

// run time: 23 seconds
// use getLines, then count the newline characters
// (redundant for this purpose, i know)
def countLines2(source: io.Source): Long = {
  val NEWLINE = 10
  var newlineCount = 0L
  for {
    line <- source.getLines
    c <- line
    if c.toByte == NEWLINE
  } newlineCount += 1
  newlineCount
}

// 12.5. How to Process a CSV File

object CSVDemo extends App {
  println("Month, Income, Expenses, Profit")
  val bufferedSource = scala.io.Source.fromFile("finance.csv")
  for (line <- bufferedSource.getLines) {
    val cols = line.split(",").map(_.trim)
    // do whatever you want with the columns here
    println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
  }
  bufferedSource.close
}
CSVDemo.main(Array())

object CSVDemo extends App {
  println("Month, Income, Expenses, Profit")

  val bufferedSource = scala.io.Source.fromFile("finance.csv")
  for (line <- bufferedSource.getLines) {
    val Array(month, revenue, expenses, profit) = line.split(",").map(_.trim)
    println(s"$month $revenue $expenses $profit")
  }
  bufferedSource.close
}
CSVDemo.main(Array())

object CSVDemo2 extends App {
  val nrows = 3
  val ncols = 4
  val rows = Array.ofDim[String](nrows, ncols)
  val bufferedSource = scala.io.Source.fromFile("finance.csv")
  var count = 0
  for (line <- bufferedSource.getLines) {
    rows(count) = line.split(",").map(_.trim)
    count += 1
  }
  bufferedSource.close

  // print the rows
  for (i <- 0 until nrows) {
    println(s"${rows(i)(0)} ${rows(i)(1)} ${rows(i)(2)} ${rows(i)(3)}")
  }
}
CSVDemo2.main(Array())

object CSVDemo2 extends App {
  val nrows = 3
  val ncols = 4
  val rows = Array.ofDim[String](nrows, ncols)
  val bufferedSource = scala.io.Source.fromFile("finance.csv")
  for ((line, count) <- bufferedSource.getLines.zipWithIndex) {
    rows(count) = line.split(",").map(_.trim)
  }
  bufferedSource.close

  // print the rows
  for (i <- 0 until nrows) {
    println(s"${rows(i)(0)} ${rows(i)(1)} ${rows(i)(2)} ${rows(i)(3)}")
  }
}
CSVDemo2.main(Array())

{
  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  import scala.collection.mutable.ArrayBuffer

  object CSVDemo3 extends App {
    // each row is an array of strings (the columns in the csv file)
    val rows = ArrayBuffer[Array[String]]()
    // (1) read the csv data
    using(scala.io.Source.fromFile("finance.csv")) { source =>
      for (line <- source.getLines) {
        rows += line.split(",").map(_.trim)
      }
    }
    // (2) print the results
    for (row <- rows) {
      println(s"${row(0)}|${row(1)}|${row(2)}|${row(3)}")
    }
  }

  CSVDemo3.main(Array())
}

// 12.6. Pretending that a String Is a File

import scala.io.Source
def printLines(source: Source) {
  for (line <- source.getLines) {
    println(line)
  }
}

val s = Source.fromString("foo\nbar\n")
printLines(s)

val f = Source.fromFile("finance.csv")
printLines(f)

package foo
object FileUtils {
  def getLinesUppercased(source: scala.io.Source): List[String] = {
    (for (line <- source.getLines) yield line.toUpperCase).toList
  }
}

import $ivy.`org.scalatest::scalatest:3.1.0`
import org.scalatest.{FunSuite, BeforeAndAfter}
import scala.io.Source
class FileUtilTests extends FunSuite with BeforeAndAfter {
  var source: Source = _
  after { source.close }

  // assumes the file has the string "foo" as its first line
  test("1 - foo file") {
    source = Source.fromFile("foo.txt")
    val lines = FileUtils.getLinesUppercased(source)
    assert(lines(0) == "FOO")
  }

  test("2 - foo string") {
    source = Source.fromString("foo\n")
    val lines = FileUtils.getLinesUppercased(source)
    assert (lines(0) == "FOO")
  }
}

// 12.7. Using Serialization

@SerialVersionUID(100L)
class Stock(var symbol: String, var price: BigDecimal) extends Serializable {
  // code here ...
}

@SerialVersionUID(114L)
class Employee extends Person with Serializable ...

import java.io._
// create a serializable Stock class
@SerialVersionUID(123L)
class Stock(var symbol: String, var price: BigDecimal) extends Serializable {
  override def toString = f"$symbol%s is ${price.toDouble}%.2f"
}
object SerializationDemo extends App {
  // (1) create a Stock instance
  val nflx = new Stock("NFLX", BigDecimal(85.00))
  // (2) write the instance out to a file
  val oos = new ObjectOutputStream(new FileOutputStream("/tmp/nflx"))
  oos.writeObject(nflx)
  oos.close
  // (3) read the object back in
  val ois = new ObjectInputStream(new FileInputStream("/tmp/nflx"))
  val stock = ois.readObject.asInstanceOf[Stock]
  ois.close
  // (4) print the object that was read back in
  println(stock)
}
SerializationDemo.main(Array())

// 12.8. Listing Files in a Directory

import java.io.File

def getListOfFiles(dir: String): List[File] = {
  val d = new File(dir)
  if (d.exists && d.isDirectory) {
    d.listFiles.filter(_.isFile).toList
  } else {
    List[File]()
  }
}
val files = getListOfFiles("/tmp")

def getListOfFiles(dir: File):List[File] = dir.listFiles.filter(_.isFile).toList

import ammonite.ops.{ls, root}
val files = ls(root / "tmp") |? { _.isFile } | { _.toIO }

import java.io.File
def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
  dir.listFiles.filter(_.isFile).toList.filter { file =>
    extensions.exists(file.getName.endsWith)
  }
}
getListOfFiles(new File("/tmp"), List("txt", "log"))

// 12.9. Listing Subdirectories Beneath a Directory

// assumes that dir is a directory known to exist

{
  def getListOfSubDirectories(dir: File): List[String] =
    dir.listFiles
      .filter(_.isDirectory)
      .map(_.getName)
      .toList

  getListOfSubDirectories(new File("/tmp")).foreach(println)
}

def getListOfSubDirectories1(dir: File): List[String] = {
  val files = dir.listFiles
  val dirNames = collection.mutable.ArrayBuffer[String]()
  for (file <- files) {
    if (file.isDirectory) {
      dirNames += file.getName
    }
  }
  dirNames.toList
}

def getListOfSubDirectories2(dir: File): List[String] = {
  val files = dir.listFiles
  val dirs = for {
    file <- files
    if file.isDirectory
  } yield file.getName
  dirs.toList
}

import ammonite.ops.{ls, root}
val files = ls(root / "tmp") |?  (_.isDir)  | ( _.last)
val dirs = ls(root / "tmp") |?  (_.isDir)  | ( _.last)
dirs.foreach(println)

// 12.10. Executing External Commands

import sys.process._
"ls -al".!
val exitCode = "ls -al".!
println(exitCode)

def playSoundFile(filename: String): Int = {
  val cmd = "afplay " + filename
  val exitCode = cmd.!
  exitCode
}

val exitCode = Seq("ls", "-al").!
val exitCode = Seq("ls", "-a", "-l").!
val exitCode = Seq("ls", "-a", "-l", "/tmp").!

val exitCode = Process("ls").!

// errors because of whitespace
" ls".!
val exitCode = Seq(" ls ", "-al").!
val exitCode = Seq("ls", " -al ").!

val process = Process("find /tmp -print").lines
process.foreach(println)

// 12.11. Executing External Commands and Using STDOUT

import sys.process._

val result = "ls -al" !!
println(result)

val result = Process("ls -al").!!
val result = Seq("ls -al").!!

val output = Seq("ls", "-al").!!
val output = Seq("ls", "-a", "-l").!!
val output = Seq("ls", "-a", "-l", "/tmp").!!

val dir = "/tmp/" // trailing / is needed otherwise no result because tmp is a link.
val searchTerm = "o"
val results = Seq("find", dir, "-type", "f", "-exec", "grep", "-il", searchTerm, "{}", ";").!!
println(results)

val out = "ls -l fred" !
val out = "ls -l fred" !!
val dir = "pwd" !!
val dir = "pwd".!!.trim

val executable = "which hadoop".!
val executable = "which hadoop2".lines_!.headOption
val executable = "which ls".lines_!.headOption

// 12.12. Handling STDOUT and STDERR for External Commands


#!/bin/sh
exec scala "$0" "$@" !#
import sys.process._
val stdout = new StringBuilder
val stderr = new StringBuilder
val status = "ls -al FRED" ! ProcessLogger(stdout append _, stderr append _)
println(status)
println("stdout: " + stdout)
println("stderr: " + stderr)

val status = Seq("find", "/usr/local", "-name", "make") ! ProcessLogger(stdout append _, stderr append _)
println(stdout)
println(stderr)

// 12.13. Building a Pipeline of Commands

import sys.process._
val numProcs = ("ps auxw" #| "wc -l").!!.trim
println(s"#procs = $numProcs")
val javaProcs = ("ps auxw" #| "grep java").!!.trim
val r = Seq("/bin/sh", "-c", "ls | grep .sc").!!

// 12.14. Redirecting the STDOUT and STDIN of External Commands

import sys.process._
import java.io.File
("ls -al" #> new File("/tmp/files.txt")).!
("ps aux" #> new File("/tmp/processes.txt")).!
("ps aux" #| "grep http" #> new File("/tmp/http-processes.out")).!
val status = ("cat /etc/passwd" #> new File("/tmp/passwd.copy")).!
println(status)

import scala.sys.process._
import java.io.File
val contents = ("cat" #< new File("/etc/passwd")).!!
println(contents)

val numLines = ("cat /etc/passwd" #> "wc -l").!!.trim
println(numLines)

// append to a file
("ps aux" #>> new File("/tmp/ps.out")).!

// 12.15. Using AND (&&) and OR (||) with Processes

val result = ("ls temp" #&& "rm temp" #|| "echo 'temp' not found").!!.trim
// ls: *.scala: No such file or directory
("ls *.scala" #&& "scalac *.scala" #|| "echo no files to compile").!

 #!/bin/sh
exec scala "$0" "$@" !#
import scala.sys.process._
val filesExist = Seq("/bin/sh", "-c", "ls *.scala")
val compileFiles = Seq("/bin/sh", "-c", "scalac *.scala")
(filesExist #&& compileFiles #|| "echo no files to compile").!!

// 12.16. Handling Wildcard Characters in External Commands

import scala.sys.process._
"ls *.sc".!
val status = Seq("/bin/sh", "-c", "ls *.sc").!
"echo *".!
Seq("grep", "-i", "foo", "*.sc").!
val status = Seq("/bin/sh", "-c", "echo *").!
val status = Seq("/bin/sh", "-c", "ls *.scala").!
val status = Seq("/bin/sh", "-c", "grep -i foo *.scala").!
val status = Seq("find", ".", "-name", "*.sc", "-type", "f").!

// 12.17. How to Run a Process in a Different Directory

import sys.process._
import java.io.File
object Test extends App {
  val output = Process("ls -al", new File("/tmp")).!!
  println(output)
}
Test.main(Array())

val p = Process("ls -al")

// 12.18. Setting Environment Variables When Running Commands

val p = Process("runFoo.sh", new File("/tmp"), "PATH" -> ".:/usr/bin:/opt/scala/bin")
val output = p.!!

val output = Process("env", None, "VAR1" -> "foo", "VAR2" -> "bar")

// 12.19. An Index of Methods to Execute External Commands

!
!!
run
lines
lines_!

#<
#>
#>>

cmd1 #! cmd2
cmd1 ### cmd2
cmd1 #> cmd2
cmd1 #&& cmd2
cmd1 #!! cmd2
cmd1 #&& cmd2 #|| cmd3

