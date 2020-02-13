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
// 20.4. Use Match Expressions and Pattern Matching
// 20.5. Eliminate null Values from Your Code
// 20.6. Using the Option/Some/None Pattern
