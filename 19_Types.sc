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
// 19.2. Creating a Method That Takes a Simple Generic Type
// 19.3. Using Duck Typing (Structural Types)
// 19.4. Make Mutable Collections Invariant
// 19.5. Make Immutable Collections Covariant
// 19.6. Create a Collection Whose Elements Are All of Some Base Type
// 19.7. Selectively Adding New Behavior to a Closed Model
// 19.8. Building Functionality with Types
