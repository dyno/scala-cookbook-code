package scalaspring

abstract class Animal(name: String) {
  def speak: Unit // asbtract
}

class Dog(name: String) extends Animal(name) {
  override def speak {
    println(name + " says Woof")
  }
}

class Cat(name: String) extends Animal(name) {
  override def speak {
    println(name + " says Meow")
  }
}
