// 2.1
/* *
 * Char    16-bit unsigned Unicode character
 * Byte    8-bit signed value
 * Short   16-bit signed value
 * Int     32-bit signed value
 * Long    64-bit signed value
 * Float   32-bit IEEE 754 single precision float
 * Double  64-bit IEEE 754 single precision float
 */

Short.MinValue
Short.MaxValue
Int.MinValue
Float.MinValue

import $ivy.`com.github.nscala-time::nscala-time:2.22.0`
import com.github.nscala_time.time.Imports._
DateTime.now // returns org.joda.time.DateTime DateTime.now + 2.months
DateTime.nextMonth < DateTime.now + 2.months
(2.hours + 45.minutes + 10.seconds).millis
DateTime.nextMonth < DateTime.now() + 2.months // returns Boolean = true
// return org.joda.time.Interval = > 2009-04-27T13:47:14.840/2009-04-28T13:47:14.840
DateTime .now() to DateTime.tomorrow
(DateTime.now() to DateTime.nextSecond).millis // returns Long = 1000

DateTime.now() // returns org.joda.time.DateTime = 2009-04-27T13:25:42.659-07:00
DateTime
  .now()
  .hour(2)
  .minute(45)
  .second(10) // returns org.joda.time.DateTime = 2009-04-27T02:45:10.313-07:00

"100".toInt
"100".toDouble
"100".toFloat
"1".toLong
"1".toShort
"1".toByte
"foo".toInt

val b = BigInt("1")
val b = BigDecimal("3.14159")

Integer.parseInt("1", 2)
Integer.parseInt("10", 2)
Integer.parseInt("100", 2)
Integer.parseInt("1", 8)
Integer.parseInt("10", 8)

implicit class StringToInt(s: String) {
  def toInt(radix: Int) = Integer.parseInt(s, radix)
}
"1".toInt(2)
"10".toInt(2)
"100".toInt(2)
"100".toInt(8)
"100".toInt(16)

@throws(classOf[NumberFormatException])
def toInt(s: String) = s.toInt

def toInt(s: String): Option[Int] = {
  try {
    Some(s.toInt)
  } catch {
    case e: NumberFormatException => None
  }
}
println(toInt("1").getOrElse(0)) // 1
println(toInt("a").getOrElse(0)) // 0
// assign the result to x
val x = toInt(aString).getOrElse(0)

toInt(aString) match {
  case Some(n) => println(n)
  case None    => println("Boom! That wasn't a number.")
}

val result = toInt(aString) match {
  case Some(x) => x
  case None    => 0 // however you want to handle this
}

// 2.2
19.45.toInt
19.toFloat
19.toDouble
19.toLong
val b = a.toFloat

val a = 1000L
a.isValidShort
a.isValidByte

// 2.3
val a = 1
val a = 1d
val a = 1f
val a = 1000L
val a = 0: Byte
val a = 0: Int
val a = 0: Short
val a = 0: Long
val a = 0: Float
val a = 0: Double
val a = 0x20
val a = 0x20L
val s = "Dave"
val p = s: Object

class Foo {
  var a: Short = 0 // specify a default value
  var b: Short = _ // defaults to 0
}

var name = null.asInstanceOf[String]

// 2.4
var a = 1
a += 1; a
a -= 1; a

var i = 1
i *= 2; i
i *= 2; i
i /= 2; i

var x = 1d
x += 1; x
var x = 1f
x += 1; x

// 2.5
def ~=(x: Double, y: Double, precision: Double) = {
  if ((x - y).abs < precision) true
  else false
}

val a = 0.3
val b = 0.1 + 0.2
~=(a, b, 0.0001)
~=(b, a, 0.0001)

0.1 + 0.2
val a = 0.3
a == b
~=(a, b, 0.000001)

object MathUtils {
  def ~=(x: Double, y: Double, precision: Double) = {
    if ((x - y).abs < precision) true
    else false
  }
}
println(MathUtils.~=(a, b, 0.000001))

// 2.6
var b = BigInt(1234567890)
var b = BigDecimal(123456.789)
b + b
b * b
b += 1; b
b.toInt
b.toLong
b.toFloat
b.toDouble
b.isValidByte
b.isValidChar
b.isValidShort
b.isValidLong
b.isValidFloat // deprecated?
b.isValidDouble // deprecated?

Byte.MaxValue
Short.MaxValue
Int.MaxValue
Long.MaxValue
Double.MaxValue
Double.PositiveInfinity
Double.NegativeInfinity
1.7976931348623157E308 > Double.PositiveInfinity

// 2.7
val r = scala.util.Random
r.nextInt
r.nextInt(100)
r.nextFloat
r.nextDouble
val r = new scala.util.Random(100)
r.setSeed(1000L)
r.nextPrintableChar
r.nextPrintableChar
var range = 0 to r.nextInt(10)
range = 0 to r.nextInt(10)

for (i <- 0 to r.nextInt(10)) yield i * 2
for (i <- 0 to r.nextInt(10)) yield (i * r.nextFloat)
for (i <- 0 to r.nextInt(10)) yield r.nextPrintableChar
for (i <- 1 to 5) yield r.nextInt(100)

// 2.8
val r = 1 to 10
val r = 1 to 10 by 2
val r = 1 to 10 by 3
for (i <- 1 to 5) println(i)
for (i <- 1 until 5) println(i)
val x = 1 to 10 toArray
val x = 1 to 10 toList
val x = (1 to 10).toList
val x = (1 to 10).toArray
var range = 0 to scala.util.Random.nextInt(10)
for (i <- 1 to 5) yield i * 2
for (i <- 1 to 5) yield i.toDouble

// 2.9
val pi = scala.math.Pi
println(f"$pi%1.5f")
f"$pi%1.5f"
f"$pi%1.2f"
f"$pi%06.2f"
"%06.2f".format(pi)

val formatter = java.text.NumberFormat.getIntegerInstance
formatter.format(10000)

val locale = new java.util.Locale("de", "DE")
val formatter = java.text.NumberFormat.getIntegerInstance(locale)
formatter.format(1000000)

val formatter = java.text.NumberFormat.getInstance
formatter.format(10000.33)

val formatter = java.text.NumberFormat.getCurrencyInstance
println(formatter.format(123.456789))
println(formatter.format(1234.56789))
println(formatter.format(12345.6789))
println(formatter.format(123456.789))

import java.util.{Currency, Locale}
val de = Currency.getInstance(new Locale("de", "DE"))
formatter.setCurrency(de)
println(formatter.format(123456.789))
