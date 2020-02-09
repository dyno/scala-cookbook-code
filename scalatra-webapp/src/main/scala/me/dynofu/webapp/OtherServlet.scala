package me.dynofu.webapp

import org.scalatra._

class StockServlet extends ScalatraServlet {
  get("/") {
    <p>Hello from StockServlet</p>
  }
}
class BondServlet extends ScalatraServlet {
  get("/") {
    <p>Hello from BondServlet</p>
  }
}
