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
// 19.4. Make Mutable Collections Invariant
// 19.5. Make Immutable Collections Covariant
// 19.6. Create a Collection Whose Elements Are All of Some Base Type
// 19.7. Selectively Adding New Behavior to a Closed Model
// 19.8. Building Functionality with Types
