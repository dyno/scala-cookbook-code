package scalaspring

import org.springframework.context.support.ClassPathXmlApplicationContext

object ScalaSpringExample extends App {
  // open & read the application context file
  val ctx = new ClassPathXmlApplicationContext("applicationContext.xml")

  // instantiate the dog and cat objects from the application context
  val dog = ctx.getBean("dog").asInstanceOf[Animal]
  val cat = ctx.getBean("cat").asInstanceOf[Animal]

  // let them speak
  dog.speak
  cat.speak
}
