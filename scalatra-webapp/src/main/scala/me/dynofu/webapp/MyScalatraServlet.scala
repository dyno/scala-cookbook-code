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
}
