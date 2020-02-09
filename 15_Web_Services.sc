// 15.1. Creating a JSON String from a Scala Object

// https://repo1.maven.org/maven2/net/liftweb/lift-json_2.12
import $ivy.`net.liftweb::lift-json:3.4+`
import net.liftweb.json.Serialization.{read => jsonRead, write => jsonWrite}

case class Address(city: String, state: String)
case class Person(name: String, address: Address)

object LiftJsonTest extends App {
  implicit val formats = Serialization.formats(NoTypeHints)
  val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))

  // serialization
  val jsonString = jsonWrite(p)
  println(jsonString)

  // deserialization
  val p1 = jsonRead[Person](jsonString)
  println(p1)
}

LiftJsonTest.main(Array())


import $ivy.`com.google.code.gson:gson:2.8.6`
import com.google.gson.Gson

case class Address(city: String, state: String)
case class Person(name: String, address: Address)

object GsonTest extends App {
  val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
  // create a JSON string from the Person, then print it
  val gson = new Gson
  val jsonString = gson.toJson(p)
  println (jsonString)
}

GsonTest.main(Array())


import net.liftweb.json.{compactRender, prettyRender}

object LiftJsonWithCollections extends App {
  val json = List(1, 2, 3)
  println(compactRender(json))
  val map = Map("fname" -> "Alvin", "lname" -> "Alexander")
  println(prettyRender(map))
}

LiftJsonWithCollections.main(Array())

// 15.2. Creating a JSON String from Classes That Have Collections

import net.liftweb.json._
import net.liftweb.json.JsonDSL._

case class Address(city: String, state: String)
case class Person(name: String, address: Address) {
  var friends = List[Person]()
}

object LiftJsonListsVersion1 extends App {
  implicit val formats = DefaultFormats

  val merc = Person("Mercedes", Address("Somewhere", "KY"))
  val mel = Person("Mel", Address("Lake Zurich", "IL"))
  val friends = List(merc, mel)
  val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
  p.friends = friends

  // define the json output
  val json = ("person" ->
    ("name" -> p.name) ~
      ("address" -> ("city" -> p.address.city) ~ ("state" -> p.address.state)) ~
      ("friends" -> friends.map { f =>
        ("name" -> f.name) ~
          ("address" -> ("city" -> f.address.city) ~ ("state" -> f.address.state))
      }))
  println(prettyRender(json))
}

LiftJsonListsVersion1.main(Array())

// 15.3. Creating a Simple Scala Object from a JSON String

import net.liftweb.json._

// a case class to represent a mail server
case class MailServer(url: String, username: String, password: String)

object JsonParsingExample extends App {
  implicit val formats = DefaultFormats
  // simulate a json string
  val jsonString = """ { "url": "imap.yahoo.com", "username": "myusername", "password": "mypassword" } """
// convert a String to a JValue object
  val jValue = parse(jsonString)
// create a MailServer object from the string
  val mailServer = jValue.extract[MailServer]
  println (mailServer.url)
  println(mailServer.username)
  println(mailServer.password)
}

JsonParsingExample.main(Array())

// 15.4. Parsing JSON Data into an Array of Objects

import net.liftweb.json.DefaultFormats
import net.liftweb.json._

// a case class to match the JSON data
case class EmailAccount(
    accountName: String,
    url: String,
    username: String,
    password: String,
    minutesBetweenChecks: Int,
    usersOfInterest: List[String])

object ParseJsonArray extends App {
  implicit val formats = DefaultFormats

  // a JSON string that represents a list of EmailAccount instances
  val jsonString = """
{
  "accounts": [
    {
      "emailAccount": {
        "accountName": "YMail",
        "username": "USERNAME",
        "password": "PASSWORD",
        "url": "imap.yahoo.com",
        "minutesBetweenChecks": 1,
        "usersOfInterest": ["barney", "betty", "wilma"]
      }
    },
    {
      "emailAccount": {
        "accountName": "Gmail",
        "username": "USER",
        "password": "PASS",
        "url": "imap.gmail.com",
        "minutesBetweenChecks": 1,
        "usersOfInterest": ["pebbles", "bam-bam"]
      }
    }
  ]
}
"""
  // json is a JValue instance
  val json = parse(jsonString)
  val elements = (json \\ "emailAccount").children
  for (acct <- elements) {
    val m = acct.extract[EmailAccount]
    println(s"Account: ${m.url}, ${m.username}, ${m.password}")
    println(" Users: " + m.usersOfInterest.mkString(","))
  }
}

// 15.5. Creating Web Services with Scalatra
// 15.6. Replacing XML Servlet Mappings with Scalatra Mounts
// 15.7. Accessing Scalatra Web Service GET Parameters
// 15.8. Accessing POST Request Data with Scalatra
// 15.9. Creating a Simple GET Request Client
// 15.10. Sending JSON Data to a POST URL
// 15.11. Getting URL Headers
// 15.12. Setting URL Headers When Sending a Request
// 15.13. Creating a GET Request Web Service with the Play Framework
// 15.14. POSTing JSON Data to a Play Framework Web Service
