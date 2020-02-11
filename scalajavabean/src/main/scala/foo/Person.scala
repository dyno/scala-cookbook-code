package foo

import scala.beans.BeanProperty

class Person(@BeanProperty var firstName: String, @BeanProperty var lastName: String) {}
class EmailAccount {
  @BeanProperty var username: String = ""
  @BeanProperty var password: String = ""
}
