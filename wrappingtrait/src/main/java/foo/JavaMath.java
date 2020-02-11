package foo;

public class JavaMath extends MathTraitWrapper {
  public static void main(String[] args) {
    new JavaMath();
  }

  public JavaMath() {
    System.out.println(sum(2, 2));
  }
}
