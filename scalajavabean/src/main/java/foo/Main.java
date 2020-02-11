package foo;

public class Main {
  public static void main(String[] args) {
    // create instances
    Person p = new Person("Regina", "Goode");
    EmailAccount acct = new EmailAccount();
    // demonstrate 'setter' methods
    acct.setUsername("regina");
    acct.setPassword("secret");
    // demonstrate 'getter' methods
    System.out.println(p.getFirstName());
    System.out.println(p.getLastName());
    System.out.println(acct.getUsername());
    System.out.println(acct.getPassword());
  }
}
