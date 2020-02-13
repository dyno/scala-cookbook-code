// 19.0 Introduction

// Variance
class Grandparent
class Parent extends Grandparent
class Child extends Parent

class InvariantClass[A]
class CovariantClass[+A]
class ContravariantClass[-A]

class VarianceExamples {
  def invarMethod(x: InvariantClass[Parent]) {}
  def covarMethod(x: CovariantClass[Parent]) {}
  def contraMethod(x: ContravariantClass[Parent]) {}

  // invarMethod(new InvariantClass[Child])
  invarMethod(new InvariantClass[Parent])
  // invarMethod(new InvariantClass[Grandparent])

  covarMethod(new CovariantClass[Child])
  covarMethod(new CovariantClass[Parent])
  // covarMethod(new CovariantClass[Grandparent])

  // contraMethod(new ContravariantClass[Child])
  contraMethod(new ContravariantClass[Parent])
  contraMethod(new ContravariantClass[Grandparent])
}

// Bounds

A <: B
A >: B
A <: Upper >: Lower

// Type Constraints

A =:= B  // A must be equal to B
A <:< B  // A must be a subtype of B
A <%< B  // A must be viewable as B

// 19.1. Creating Classes That Use Generic Types

class LinkedList[A] {
  private class Node[A](elem: A) {
    var next: Node[A] = _
    override def toString = elem.toString
  }
  private var head: Node[A] = _
  def add(elem: A) {
    val n = new Node(elem)
    n.next = head
    head = n
  }
  private def printNodes(n: Node[A]) {
    if (n != null) {
      println(n)
      printNodes(n.next)
    }
  }
  def printAll() { printNodes(head) }
}

val ints = new LinkedList[Int]()
ints.add(1)
ints.add(2)

val strings = new LinkedList[String]()
strings.add("Nacho")
strings.add("Libre")
strings.printAll()

trait Animal
class Dog extends Animal { override def toString = "Dog" }
class SuperDog extends Dog { override def toString = "SuperDog" }
class FunnyDog extends Dog { override def toString = "FunnyDog" }
val dogs = new LinkedList[Dog]
val fido = new Dog
val wonderDog = new SuperDog
val scooby = new FunnyDog
dogs.add(fido)
dogs.add(wonderDog)
dogs.add(scooby)

def printDogTypes(dogs: LinkedList[Dog]) { dogs.printAll() }
printDogTypes(dogs)

val superDogs = new LinkedList[SuperDog]
superDogs.add(wonderDog)
// error: this line won't compile
printDogTypes(superDogs)

// from http://docs.oracle.com/javase/tutorial/java/generics/types.html
public interface Pair<K, V> {
  public K getKey();
  public V getValue();
}

trait Pair[A, B] {
  def getKey: A
  def getValue: B
}

// 19.2. Creating a Method That Takes a Simple Generic Type

def randomName(names: Seq[String]): String = {
  val randomNum = util.Random.nextInt(names.length)
  names(randomNum)
}

val names = Seq("Aleka", "Christina", "Tyler", "Molly")
val winner = randomName(names)

def randomElement[A](seq: Seq[A]): A = {
  val randomNum = scala.util.Random.nextInt(seq.length)
  seq(randomNum)
}

// specify the return type is not necessary
def randomElement[A](seq: Seq[A]) = {
  val randomNum = scala.util.Random.nextInt(seq.length)
  seq(randomNum)
}

randomElement(Seq("Aleka", "Christina", "Tyler", "Molly"))
randomElement(List(1, 2, 3))
randomElement(List(1.0, 2.0, 3.0))
randomElement(Vector.range('a', 'z'))

// 19.3. Using Duck Typing (Structural Types)

class Dog { def speak() { println("woof") } }
class Klingon { def speak() { println("Qapla!") } }

object DuckTyping extends App {
  // XXX: As a word of warning, this technique uses reflection, so you may not want to use it when performance is a concern.
  def callSpeak[A <: { def speak(): Unit }](obj: A) {
    obj.speak()
  }

  callSpeak(new Dog)
  callSpeak(new Klingon)
}

DuckTyping.main(Array())

// 19.4. Make Mutable Collections Invariant

trait Animal { def speak }
class Dog(var name: String) extends Animal {
  def speak { println("woof") }
  override def toString = name
}
class SuperDog(name: String) extends Dog(name) {
  def useSuperPower { println("Using my superpower!") }
}

val fido = new Dog("Fido")
val wonderDog = new SuperDog("Wonder Dog") 
val shaggy = new SuperDog("Shaggy")

import scala.collection.mutable.ArrayBuffer

val dogs = ArrayBuffer[Dog]() 
dogs += fido
dogs += wonderDog

def makeDogsSpeak(dogs: ArrayBuffer[Dog]) {
  dogs.foreach(_.speak)
}

makeDogsSpeak(dogs)

val superDogs = ArrayBuffer[SuperDog]()
superDogs += shaggy
superDogs += wonderDog
makeDogsSpeak(superDogs) // ERROR: won't compile

// Mutable -> invariant
class Array[T]
class ArrayBuffer[A]
class ListBuffer[A]

// Immutable -> covariant
class List[+T]
class Vector[+A]
trait Seq[+A]

// 19.5. Make Immutable Collections Covariant

trait Animal {
  def speak
}
class Dog(var name: String) extends Animal {
  def speak { println("Dog says woof") }
}
class SuperDog(name: String) extends Dog(name) {
  override def speak { println("I'm a SuperDog") }
}

def makeDogsSpeak(dogs: Seq[Dog]) {
  dogs.foreach(_.speak)
}

// this works
val dogs = Seq(new Dog("Fido"), new Dog("Tanner"))
makeDogsSpeak(dogs)

// this works too
val superDogs = Seq(new SuperDog("Wonder Dog"), new SuperDog("Scooby"))
makeDogsSpeak(superDogs)


class Container[+A](val elem: A)
def makeDogsSpeak(dogHouse: Container[Dog]) {
  dogHouse.elem.speak
}
val dogHouse = new Container(new Dog("Tanner"))
makeDogsSpeak(dogHouse)
val superDogHouse = new Container(new SuperDog("Wonder Dog"))
makeDogsSpeak(superDogHouse)

// 19.6. Create a Collection Whose Elements Are All of Some Base Type

trait CrewMember
class Officer extends CrewMember
class RedShirt extends CrewMember
trait Captain
trait FirstOfficer
trait ShipsDoctor
trait StarfleetTrained

val kirk = new Officer with Captain
val spock = new Officer with FirstOfficer 
val bones = new Officer with ShipsDoctor

val officers = new Crew[Officer]() 
officers += kirk
officers += spock
officers += bones

val redShirt = new RedShirt
officers += redShirt // ERROR: this won't compile
class Crew[A <: CrewMember] extends ArrayBuffer[A]
val officers = new Crew[Officer]()
// error: won't compile
val officers = new Crew[String]()
val redshirts = new Crew[RedShirt]()

class Crew[A <: CrewMember with StarfleetTrained] extends ArrayBuffer[A]
val kirk = new Officer with Captain with StarfleetTrained
val spock = new Officer with FirstOfficer with StarfleetTrained 
val bones = new Officer with ShipsDoctor with StarfleetTrained
val officers = new Crew[Officer with StarfleetTrained]() 
officers += kirk
officers += spock
officers += bones

class StarfleetOfficer extends Officer with StarfleetTrained
val kirk = new StarfleetOfficer with Captain

trait CrewMember {
  def beamDown { println("beaming down") }
}
class RedShirt extends CrewMember {
  def putOnRedShirt { println("putting on my red shirt") }
}

def beamDown[A <: CrewMember](crewMember: Crew[A]) {
  crewMember.foreach(_.beamDown)
}

def getReadyForDay[A <: RedShirt](redShirt: Crew[A]) {
  redShirt.foreach(_.putOnRedShirt)
}

//  <% view bounnd
//  <: upper bound

// 19.7. Selectively Adding New Behavior to a Closed Model

def add[A](x: A, y: A)(implicit numeric: Numeric[A]): A = numeric.plus(x, y)
add(1, 1)
add(1.0, 1.5) 
add(1, 1.5F)

// Creating a type class

package typeclassdemo
// an existing, closed model
trait Animal
final case class Dog(name: String) extends Animal
final case class Cat(name: String) extends Animal

object Humanish {
// the type class.
// defines an abstract method named 'speak'.
  trait HumanLike[A] {
    def speak(speaker: A): Unit
  }
  // companion object
  object HumanLike {
    // implement the behavior for each desired type. in this case, only for a Dog.
    implicit object DogIsHumanLike extends HumanLike[Dog] {
      def speak(dog: Dog) { println(s"I'm a Dog, my name is ${dog.name}") }
    }
  }
}

object TypeClassDemo extends App {
  import Humanish.HumanLike

  // create a method to make an animal speak
  def makeHumanLikeThingSpeak[A](animal: A)(implicit humanLike: HumanLike[A]) {
    humanLike.speak(animal)
  }

  // because HumanLike implemented this for a Dog, it will work
  makeHumanLikeThingSpeak(Dog("Rover"))
  // however, the method won't compile for a Cat (as desired)
  //makeHumanLikeThingSpeak(Cat("Morris"))
}

TypeClassDemo.main(Array())

// 19.8. Building Functionality with Types

// Example 1: Creating a Timer

def timer[A](blockOfCode: => A) = {
  val startTime = System.nanoTime
  val result = blockOfCode
  val stopTime = System.nanoTime
  val delta = stopTime - startTime
  (result, delta / 1000000d)
}

val (result, time) = timer{ println("Hello") }
println(s"result: $result, time: $time")

def readFile(filename: String) = scala.io.Source.fromFile(filename).getLines
val (result, time) = timer{ readFile("/etc/passwd") }

// Example 2: Writing Your Own “Try” Classes

// version 1

{
  sealed class Attempt[A]
  final case class Failed[A](val exception: Throwable) extends Attempt[A]
  final case class Succeeded[A](value: A) extends Attempt[A]

  object Attempt {
    def apply[A](f: => A): Attempt[A] =
      try {
        val result = f
        return Succeeded(result)
      } catch {
        case e: Exception => Failed(e)
      }
  }
}

val x = Attempt("10".toInt) // Succeeded(10)
val y = Attempt("10A".toInt) // Failed(Exception)

// version 2

{
  sealed abstract class Attempt[A] {
    var isSuccess = false
    // B >: A is a lower bound
    def getOrElse[B >: A](default: => B): B = if (isSuccess) get else default
    def get: A
  }

  object Attempt {
    def apply[A](f: => A): Attempt[A] =
      try {
        val result = f
        Succeeded(result)
      } catch {
        case e: Exception => Failed(e)
      }
  }

  final case class Failed[A](val exception: Throwable) extends Attempt[A] {
    isSuccess = false
    def get: A = throw exception
  }

  final case class Succeeded[A](result: A) extends Attempt[A] {
    isSuccess = true
    def get = result
  }
}

// The Scala 2.10 Try classes

def getOrElse[U >: T](default: => U): U = if (isSuccess) get else default
def map[U](f: T => U): Try[U] = Try[U](f(value))
// A <:< B “A must be a subtype of B.”
def flatten[U](implicit ev: T <:< Try[U]): Try[U] = value
