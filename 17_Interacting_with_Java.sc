// 17.1. Going to and from Java Collections
// https://stackoverflow.com/questions/8301947/what-is-the-difference-between-javaconverters-and-javaconversions-in-scala
// repl.compiler.settings.deprecation.value = true
// import scala.collection.JavaConversions._
// object JavaConversions in package collection is deprecated (since 2.12.0): use JavaConverters
// list.foreach(println)

import scala.collection.JavaConverters._

def nums = {
  var list = new java.util.ArrayList[Int]()
  list.add(1)
  list.add(2)
  list
}

val list = nums
list.asScala.foreach(println)

import scala.collection.JavaConversions.asScalaBuffer
val numbers = asScalaBuffer(list)
numbers.foreach(println)

val peeps = new java.util.HashMap[String, String]()
peeps.put("captain", "Kirk")
peeps.put("doctor", "McCoy")

val peeps1 = peeps.asScala
import scala.collection.JavaConversions.mapAsScalaMap
val peeps2 = mapAsScalaMap(peeps)

// java

/* public static int sum(List<Integer> list) {
 *   int sum = 0;
 *   for (int i: list) { sum = sum + i; }
 *
 *   return sum;
 * }
 */

val sum = (list:java.util.List[Integer]) => {
  var sigma:Integer = 0
  for (i <- list.asScala) { sigma += i }
  sigma
}

import scala.collection.JavaConverters.seqAsJavaList
sum(seqAsJavaList(List(1, 2, 3)))

import scala.collection.JavaConverters.{seqAsJavaList, bufferAsJavaList}
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
val sum1 = sum(seqAsJavaList(Seq(1, 2, 3)))
val sum2 = sum(bufferAsJavaList(ArrayBuffer(1,2,3)))
val sum3 = sum(bufferAsJavaList(ListBuffer(1,2,3)))

// 17.2. Add Exception Annotations to Scala Methods to Work with Java

// scala
class Thrower {
  @throws(classOf[Exception])
  def exceptionThrower {
    throw new Exception("Exception!")
  }
}

// multiple exceptions
@throws(classOf[IOException])
@throws(classOf[LineUnavailableException])
@throws(classOf[UnsupportedAudioFileException])
def playSoundFileWithJavaAudio {
  // exception throwing code here ...
}


// 17.3. Using @SerialVersionUID and Other Annotations

// scala
// deprecated
@remote trait Hello {
  def sayHello(): String
}

// the new way
@throws[java.rmi.RemoteException]
trait Hello extends java.rmi.Remote {
}

// java
public interface Hello extends java.rmi.Remote {
  String sayHello() throws java.rmi.RemoteException;
}

// 17.4. Using the Spring Framework

// see ./scalaspring

// 17.5. Annotating varargs Methods

package varargs

import scala.annotation.varargs
class Printer {
  @varargs def printAll(args: String*) {
    args.foreach(print)
    println
  }
}

// Main.java
public class Main {
  public static void main(String[] args) {
    Printer p = new Printer();
    p.printAll("Hello");
    p.printAll("Hello, ", "world");
  }
}

// 17.6. When Java Code Requires JavaBeans

import scala.bean.BeanProperty
class Person(@BeanProperty var firstName: String, @BeanProperty var lastName: String) {
  override def toString = s"Person: $firstName $lastName"
}

import scala.bean.BeanProperty
class EmailAccount {
  @BeanProperty var username: String = ""
  @BeanProperty var password: String = ""
  override def toString = s"Email Account: ($username, $password)"
}

// scalajavabean/

// 17.7. Wrapping Traits with Implementations
//
