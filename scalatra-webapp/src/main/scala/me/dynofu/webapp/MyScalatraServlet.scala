package me.dynofu.webapp

import org.scalatra._
import net.liftweb.json._

case class Person(firstName: String, lastName: String, age: Int)

class MyScalatraServlet extends ScalatraServlet {
  get("/") {
    <html>
      <body>
      <h1>Hello, world!</h1>
      Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  get("/hello") {
    <p>Hello, world!</p>
  }

  /**
   * The URL
   * http://localhost:8080/saveName?fname=Alvin&lname=Alexander
   * prints: Some(Alvin), Some(Alexander)
   */
  get("/saveName") {
    val firstName = params.get("fname")
    val lastName = params.get("lname")

    <p>{firstName}, {lastName}</p>
  }

  get("/hello/:fname/:lname") {
    <p>Hello, {params("fname")}, {params("lname")}</p>
  }

  /**
   * (1) GET http://localhost:8080/getFilename/Users/Al/tmp/file.txt
   */
  get("/getFilename/*.*") {
    // (2) creates a Vector(Users/Al/tmp/file, txt)
    val data = multiParams("splat")

    // (3) prints: [Users/Al/tmp/file, txt]
    <pre>{data.mkString("[", ", ", "]")}</pre>
  }

    /**
   * Expects an incoming JSON string like this:
   * {"firstName":"FN","lastName":"LN", "age": 18}
   */
  post("/posttest") {
    implicit val formats = DefaultFormats

    // get the POST request data
    val jsonString = request.body
    // convert the JSON string to a JValue object
    val jValue = parse(jsonString)
    // deserialize the string into a Stock object
    val p = jValue.extract[Person]
    // you can send information back to the client in the response header
    response.addHeader("ACK", s"GOT $p")
  }

}
