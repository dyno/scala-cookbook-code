package foo

// the original trait
trait MathTrait {
  def sum(x: Int, y: Int) = x + y
}

// the wrapper class
class MathTraitWrapper extends MathTrait
