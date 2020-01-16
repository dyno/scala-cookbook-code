// scala
def doSomething(x: Int): String = {
   "code here"
}

def plusOne(i: Int) = i + 1

// 5.1

class Foo {
  private[this] def isFoo = true
  def doFoo(other: Foo) {
    if (other.isFoo) { // this line won't compile
      // ...
    }
  }
}

class Foo {
  private def isFoo = true
  def doFoo(other: Foo) {
    if (other.isFoo) { // this now compiles
      // ...
    }
  }
}

{
  class Animal {
    private def heartBeat {}
  }
  class Dog extends Animal {
    heartBeat // won't compile
  }
}

{
  class Animal {
    protected def breathe {}
  }
  class Dog extends Animal {
    breathe
  }
}

package world
{
  class Animal {
    protected def breathe {}
  }
  class Jungle {
    val a = new Animal
    a.breathe // error: this line won't compile
  }
}

package com.acme.coolapp.model
{
  class Foo {
    private[model] def doX {}
    private def doY {}
  }
  class Bar {
    val f = new Foo
    f.doX // compiles f.doY // won't compile
  }
}

package com.acme.coolapp.model
{
  class Foo {
    private[model] def doX {}
    private[coolapp] def doY {}
    private[acme] def doZ {}
  }
}
import com.acme.coolapp.model._
package com.acme.coolapp.view
{
  class Bar {
    val f = new Foo
    f.doX // won't compile
    f.doY
    f.doZ
  }
}
package com.acme.common
{
  class Bar {
    val f = new Foo
    f.doX // won't compile
    f.doY // won't compile
    f.doZ
  }
}

package com.acme.coolapp.model
{
  class Foo {
    def doX {}
  }
}
package org.xyz.bar
{
  class Bar {
    val f = new com.acme.coolapp.model.Foo
    f.doX
  }
}

// 5.2

class WelcomeActivity extends Activity {
  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    // more code here ...
  }
}

{
  class FourLeggedAnimal {
    def walk { println("I'm walking") }
    def run { println("I'm running") }
  }

  class Dog extends FourLeggedAnimal {
    def walkThenRun {
      super.walk
      super.run
    }
  }

  val suka = new Dog
  suka.walkThenRun
}

{
  trait Human { def hello = "the Human trait" }
  trait Mother extends Human { override def hello = "Mother" }
  trait Father extends Human { override def hello = "Father" }

  class Child extends Human with Mother with Father {
    def printSuper = super.hello
    def printMother = super[Mother].hello
    def printFather = super[Father].hello
    def printHuman = super[Human].hello
  }

  object Test extends App {
    val c = new Child
    println(s"c.printSuper = ${c.printSuper}")
    println(s"c.printMother = ${c.printMother}")
    println(s"c.printFather = ${c.printFather}")
    println(s"c.printHuman = ${c.printHuman}")
  }

  Test.main(Array())
}

{
  trait Animal {
    def walk { println("Animal is walking") }
  }
  class FourLeggedAnimal extends Animal {
    override def walk { println("I'm walking on all fours") }
  }
  class Dog extends FourLeggedAnimal {
    def walkThenRun {
      super.walk // works
      super[FourLeggedAnimal].walk // works
      super[Animal].walk // error: won't compile
    }
  }
}

// 5.3
