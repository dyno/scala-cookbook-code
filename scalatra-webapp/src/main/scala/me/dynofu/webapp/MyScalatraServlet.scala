package me.dynofu.webapp

import org.scalatra._

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

}
