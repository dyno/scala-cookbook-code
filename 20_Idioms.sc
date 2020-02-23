// 20.1. Create Methods with No Side Effects (Pure Functions)
case class Stock(symbol: String, company: String)
case class StockInstance(symbol: String, datetime: String, price: BigDecimal, volume: Long)

StockInstance("AAPL", "Nov. 2, 2012 5:00pm", 576.80, 20431707)

object NetworkUtils {
  def getUrlContent(url: String): String = { ... }
}

object StockUtils {
  def buildUrl(stockSymbol: String): String = { ... }
  def getPrice(html: String): String = { ... }
  def getVolume(symbol: String, html: String): String = { ... }
  def getHigh(symbol: String, html: String): String = { ... }
  def getLow(symbol: String, html: String): String = { ... }
}

object DateUtils {
  def currentDate: String = { ... }
  def currentTime: String = { ... }
}

val stock = new Stock("AAPL", "Apple")
val url = StockUtils.buildUrl(stock.symbol)
val html = NetUtils.getUrlContent(url)
val price = StockUtils.getPrice(html)
val volume = StockUtils.getVolume(html)
val high = StockUtils.getHigh(html)
val low = StockUtils.getLow(html)
val date = DateUtils.currentDate
val stockInstance = StockInstance(symbol, date, price, volume, high, low)

val html = NetUtils.getUrlContent(url)
val stockInstance = StockInstance(
  symbol,
  DateUtils.currentDate,
  StockUtils.getPrice(html),
  StockUtils.getVolume(html),
  StockUtils.getHigh(html),
  StockUtils.getLow(html))

object StockDao {
  def getStockInstances(symbol: String): Vector[StockInstance] = { ... }
  // other code ...
}

val url = NetUtils.buildUrl("AAPL")
val html = NetUtils.getUrlContent(url)
val price = StockUtils.getPrice(html)
assert(price == 500.0)

// StockUtils or Stock object?
case class Stock(symbol: String, company: String)
object Stock {
  def buildUrl(stockSymbol: String): String = { ... }
  def getPrice(symbol: String, html: String): String = { ... }
  def getVolume(symbol: String, html: String): String = { ... }
  def getHigh(symbol: String, html: String): String = { ... }
  def getLow(symbol: String, html: String): String = { ... }
}

// 20.2. Prefer Immutable Objects

// java
class EvilMutator {
  // trust me ... mu ha ha (evil laughter)
  public static void trustMeMuHaHa(final List<Person> people) {
    people.clear();
  }
}

def evilMutator(people: ArrayBuffer[Person]) {
  people.clear()
}

def evilMutator(people: Vector[Person]) {
  // ERROR - won't compile
  people.clear()
}

// Using val + mutable, and var + immutable

private val timers = mutable.Map[String, Timer]()
// some time later ...
timers -= name
timers.clear()

class Pizza {
  private val _toppings = new collection.mutable.ArrayBuffer[Topping]()

  def toppings = _toppings.toList
  def addTopping(t: Topping) { _toppings += t }
  def removeTopping(t: Topping) { _toppings -= t }
}


// 20.3. Think "Expression-Oriented Programming"

// a series of expressions

val url = StockUtils.buildUrl(symbol)
val html = NetUtils.getUrlContent(url)
val price = StockUtils.getPrice(html)
val volume = StockUtils.getVolume(html)
val high = StockUtils.getHigh(html)
val low = StockUtils.getLow(html)
val date = DateUtils.getDate
val stockInstance = StockInstance(symbol, date, price, volume, high, low)

val evenOrOdd = i match {
  case 1 | 3 | 5 | 7 | 9 => println("odd")
  case 2 | 4 | 6 | 8 | 10 => println("even")
}
val result =
  try {
    "1".toInt
  } catch {
    case _ => 0
  }

// 20.4. Use Match Expressions and Pattern Matching

val month = i match {
  case 1 => "January"
  case 2 => "February"
  case 3 => "March"
  case 4 => "April"
  case 5 => "May"
  case 6 => "June"
  case 7 => "July"
  case 8 => "August"
  case 9 => "September"
  case 10 => "October"
  case 11 => "November"
  case 12 => "December"
  case _ => "Invalid month" // the default, catch-all
}

i match {
  case 1 | 3 | 5 | 7 | 9 => println("odd")
  case 2 | 4 | 6 | 8 | 10 => println("even")
}

def readTextFile(filename: String): Option[List[String]] =
  try {
    Some(Source.fromFile(filename).getLines.toList)
  } catch {
    case e: Exception => None
  }

def readTextFile(filename: String): Option[List[String]] =
  try {
    Some(Source.fromFile(filename).getLines.toList)
  } catch {
    case ioe: IOException =>
      logger.error(ioe)
      None
    case fnf: FileNotFoundException =>
      logger.error(fnf)
      None
  }

def isTrue(a: Any) = a match {
  case 0 | "" => false
  case _ => true
}

def getClassAsString(x: Any): String = x match {
  case s: String => "String"
  case i: Int => "Int"
  case l: List[_] => "List"
  case p: Person => "Person"
  case Dog() => "That was a Dog"
  case Parrot(name) => s"That was a Parrot, name = $name"
  case _ => "Unknown"
}

val divide: PartialFunction[Int, Int] = {
  case d: Int if d != 0 => 42 / d
}

def toInt(s: String): Option[Int] =
  try {
    Some(s.toInt)
  } catch {
    case e: Exception => None
  }

toInt(aString) match {
  case Some(i) => println(i)
  case None => println("Error: Could not convert String to Int.")
}


verifying(
  "If age is given, it must be greater than zero",
  model =>
    model.age match {
      case Some(age) => age < 0
      case None => true
    })

class SarahsBrain extends Actor {
  def receive = {
    case StartMessage => handleStartMessage
    case StopMessage => handleStopMessage
    case SetMaxWaitTime(time) => helper ! SetMaxWaitTime(time)
    case SetPhrasesToSpeak(phrases) => helper ! SetPhrasesToSpeak(phrases)
    case _ => log.info("Got something unexpected.")
  }
// other code here ...
}

// 20.5. Eliminate null Values from Your Code

case class Address(city: String, state: String, zip: String)
class User(email: String, password: String) {
  var firstName = None: Option[String]
  var lastName = None: Option[String]
  var address = None: Option[Address]
}

val u = new User("al@example.com", "secret")

u.firstName = Some("Al")
u.lastName = Some("Alexander")
u.address = Some(Address("Talkeetna", "AK", "99676"))

println(firstName.getOrElse("<not assigned>"))

u.address.foreach { a =>
  println(a.city)
  println(a.state)
  println(a.zip)
}

case class Stock(id: Long, var symbol: String, var company: Option[String])

def doSomething: Option[String] = { ... }
def toInt(s: String): Option[Int] = { ... }
def lookupPerson(name: String): Option[Person] = { ... }

def readTextFile(filename: String): Option[List[String]] =
  try {
    Some(io.Source.fromFile(filename).getLines.toList)
  } catch {
    case e: Exception => None
  }

import scala.util.{Failure, Success, Try}

object Test extends App {
  def readTextFile(filename: String): Try[List[String]] = Try(scala.io.Source.fromFile(filename).getLines.toList)
  val filename = "/etc/passwd"
  readTextFile(filename) match {
    case Success(lines) => lines.foreach(println)
    case Failure(f) => println(f)
  }
}

Test.main(Array())

// Null Object Pattern
trait Animal { def makeSound() }
class Dog extends Animal {
  def makeSound() { println("woof") }
}
class NullAnimal extends Animal {
  def makeSound() {}
}

def getName: Option[String] = {
  var name = javaPerson.getName
  if (name == null) None else Some(name)
}

// 20.6. Using the Option/Some/None Pattern

def toInt(s: String): Option[Int] =
  try {
    Some(Integer.parseInt(s.trim))
  } catch {
    case e: Exception => None
  }

val x = toInt("1")
val x = toInt("foo")

val x = toInt("1").getOrElse(0)
toInt("1").foreach(i => println(s"Got an int: $i"))
toInt("1") match {
  case Some(i) => println(i)
  case None => println("That didn't work.")
}

val bag = List("1", "2", "foo", "3", "bar")
bag.map(toInt)
bag.map(toInt).flatten
bag.flatMap(toInt)
bag.map(toInt).collect { case Some(i) => i }

def getAll(): List[Stock] =
  DB.withConnection { implicit connection =>
    sqlQuery().collect {
      // the 'company' field has a value
      case Row(id: Int, symbol: String, Some(company: String)) =>
        Stock(id, symbol, Some(company))
      // the 'company' field does not have a value
      case Row(id: Int, symbol: String, None) =>
    }.toList
  }

verifying(
  "If age is given, it must be greater than zero",
  model =>
    model.age match {
      case Some(age) => age < 0
      case None => true
    })

import scala.util.control.Exception._
def readTextFile(f: String): Option[List[String]] =
  allCatch.opt(Source.fromFile(f).getLines.toList)

import scala.util.{Failure, Success, Try}
def divideXByY(x: Int, y: Int): Try[Int] = Try(x / y)
divideXByY(1, 1)
divideXByY(1, 0)
divideXByY(1, 1).foreach(println)
divideXByY(1, 0).foreach(println)
divideXByY(1, 1) match {
  case Success(i) => println(s"Success, value is: $i")
  case Failure(s) => println(s"Failed, message is: $s")
}

val x = divideXByY(1, 0)
if (x.isFailure) x.toString

val z = for {
  a <- Try(x.toInt)
  b <- Try(y.toInt)
} yield a * b

val answer = z.getOrElse(0) * 2

def readTextFile(filename: String): Try[List[String]] =
  Try(Source.fromFile(filename).getLines.toList)

// Either/Left/Right

def divideXByY(x: Int, y: Int): Either[String, Int] =
  if (y == 0) Left("Dude, can't divide by 0") else Right(x / y)

val x = divideXByY(1, 1).right.getOrElse(0) // returns 1
val x = divideXByY(1, 0).right.getOrElse(0) // returns 0
// prints "Answer: Dude, can't divide by 0"
divideXByY(1, 0) match {
  case Left(s) => println("Answer: " + s)
  case Right(i) => println("Answer: " + i)
}

val x = divideXByY(1, 0)
x.isLeft
x.left
