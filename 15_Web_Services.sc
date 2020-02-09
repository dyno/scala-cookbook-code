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

ParseJsonArray.main(Array())

// 15.5. Creating Web Services with Scalatra
// 15.6. Replacing XML Servlet Mappings with Scalatra Mounts
// 15.7. Accessing Scalatra Web Service GET Parameters

// 15.8. Accessing POST Request Data with Scalatra
import $ivy.`net.liftweb::lift-json:3.4+`
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import $ivy.`org.apache.httpcomponents:httpclient:4.5.11`
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient

class Stock(var symbol: String, var price: Double)

object PostTester extends App {
  // create a Stock and convert it to a JSON string
  val stock = new Stock("AAPL", 500.00)
  implicit val formats = DefaultFormats
  val stockAsJsonString = write(stock)
  // add the JSON string as a StringEntity to a POST request
  val post = new HttpPost("http://localhost:8080/stocks/saveJsonStock")
  post.setHeader("Content-type", "application/json")
  post.setEntity(new StringEntity(stockAsJsonString))
  // send the POST request
  val response = (new DefaultHttpClient).execute(post)
  // print the response
  println("--- HEADERS ---")
  response.getAllHeaders.foreach(arg => println(arg))
}

PostTester.main(Array())

// 15.9. Creating a Simple GET Request Client

/**
 * Returns the text (content) from a URL as a String.
 * Warning: This method does not time out when the service is non-responsive.
 */
@throws(classOf[java.io.IOException])
def get(url: String) = scala.io.Source.fromURL(url).mkString
val r = get("http://localhost:8080/hello")

/**
* Returns the text (content) from a REST URL as a String.
* Inspired by http://matthewkwong.blogspot.com/2009/09/scala-scalaiosource-fromurl-blockshangs.html
* and http://alvinalexander.com/blog/post/java/how-open-url-read-contents-httpurl-connection-java
*
* The `connectTimeout` and `readTimeout` comes from the Java URLConnection
* class Javadoc.
* @param url The full URL to connect to.
* @param connectTimeout Sets a specified timeout value, in milliseconds,
* to be used when opening a communications link to the resource referenced
* by this URLConnection. If the timeout expires before the connection can
* be established, a java.net.SocketTimeoutException
* is raised. A timeout of zero is interpreted as an infinite timeout.
* Defaults to 5000 ms.
* @param readTimeout If the timeout expires before there is data available
* for read, a java.net.SocketTimeoutException is raised. A timeout of zero
* is interpreted as an infinite timeout. Defaults to 5000 ms.
* @param requestMethod Defaults to "GET". (Other methods have not been tested.) *
* @example get("http://www.example.com/getInfo")
* @example get("http://www.example.com/getInfo", 5000)
* @example get("http://www.example.com/getInfo", 5000, 5000)
*/

@throws(classOf[java.io.IOException])
@throws(classOf[java.net.SocketTimeoutException])
def get(url: String, connectTimeout: Int = 5000, readTimeout: Int = 5000, requestMethod: String = "GET") = {
  import java.net.{URL, HttpURLConnection}
  val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
  connection.setConnectTimeout(connectTimeout)
  connection.setReadTimeout(readTimeout)
  connection.setRequestMethod(requestMethod)
  val inputStream = connection.getInputStream
  val content = scala.io.Source.fromInputStream(inputStream).mkString
  if (inputStream != null) inputStream.close
  content
}
val r = get("http://localhost:8080/hello")

try {
  val content = get("http://localhost:8080/waitForever")
  println(content)
} catch {
  case ioe: java.io.IOException => // handle this
    println(ioe)
  case ste: java.net.SocketTimeoutException => // handle this
    println(ste)
}

// Using the Apache HttpClient

import java.io._
import org.apache.http.{HttpEntity, HttpResponse}
import org.apache.http.client._
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import scala.collection.mutable.StringBuilder
import scala.xml.XML
import org.apache.http.params.HttpConnectionParams
import org.apache.http.params.HttpParams

def buildHttpClient(connectionTimeout: Int, socketTimeout: Int): DefaultHttpClient = {
  val httpClient = new DefaultHttpClient
  val httpParams = httpClient.getParams
  HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout)
  HttpConnectionParams.setSoTimeout(httpParams, socketTimeout)
  httpClient.setParams(httpParams)
  httpClient
}

/**
 * Returns the text (content) from a REST URL as a String.
 * Returns a blank String if there was a problem.
 * This function will also throw exceptions if there are problems trying
 * to connect to the url. *
 * @param url A complete URL, such as "http://foo.com/bar"
 * @param connectionTimeout The connection timeout, in ms.
 * @param socketTimeout The socket timeout, in ms.
 */
def getRestContent(url: String, connectionTimeout: Int, socketTimeout: Int): String = {
  val httpClient = buildHttpClient(connectionTimeout, socketTimeout)
  val httpResponse = httpClient.execute(new HttpGet(url))
  val entity = httpResponse.getEntity
  var content = ""
  if (entity != null) {
    val inputStream = entity.getContent
    content = scala.io.Source.fromInputStream(inputStream).getLines.mkString
    inputStream.close
  }
  httpClient.getConnectionManager.shutdown
  content
}

getRestContent("http://localhost:8080/hello", 1000, 500)

// 15.10. Sending JSON Data to a POST URL

import java.io._
import java.util.ArrayList
import $ivy.`org.apache.httpcomponents:httpclient:4.5.11`
import org.apache.commons._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import $ivy.`com.google.code.gson:gson:2.8.6`
import com.google.gson.Gson

case class Person(firstName: String, lastName: String, age: Int)
object HttpJsonPostTest extends App {
  // create our object as a json string
  val spock = new Person("Leonard", "Nimoy", 82)
  val spockAsJson = new Gson().toJson(spock)
  // add name value pairs to a post object
  val post = new HttpPost("http://localhost:8080/posttest")
  val nameValuePairs = new ArrayList[NameValuePair]()
  nameValuePairs.add(new BasicNameValuePair("JSON", spockAsJson))
  post.setEntity(new UrlEncodedFormEntity(nameValuePairs))
  // send the post request
  val client = new DefaultHttpClient
  val response = client.execute(post)
  println("--- HEADERS ---")
  response.getAllHeaders.foreach(arg => println(arg))
}

HttpJsonPostTest.main(Array())

// 15.11. Getting URL Headers

import $ivy.`org.apache.httpcomponents:httpclient:4.5.11`
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
object FetchUrlHeaders extends App {
  val get = new HttpGet("http://alvinalexander.com/")
  val client = new DefaultHttpClient
  val response = client.execute(get)
  response.getAllHeaders.foreach(header => println(header))
}

FetchUrlHeaders.main(Array())

// 15.12. Setting URL Headers When Sending a Request

import $ivy.`org.apache.httpcomponents:httpclient:4.5.11`
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
object SetUrlHeaders extends App {
  val url = "http://localhost:9001/baz"
  val httpGet = new HttpGet(url)
  // set the desired header values
  httpGet.setHeader("KEY1", "VALUE1")
  httpGet.setHeader("KEY2", "VALUE2")
  // execute the request
  val client = new DefaultHttpClient
  client.execute(httpGet)
  client.getConnectionManager.shutdown
}

SetUrlHeaders.main(Array())

// 15.13. Creating a GET Request Web Service with the Play Framework


// 15.14. POSTing JSON Data to a Play Framework Web Service
