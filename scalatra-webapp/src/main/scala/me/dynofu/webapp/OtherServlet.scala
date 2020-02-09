package me.dynofu.webapp

import org.scalatra._
import net.liftweb.json._


class StockServlet extends ScalatraServlet {
  get("/") {
    <p>Hello from StockServlet</p>
  }

  /**
   * Expects an incoming JSON string like this:
   * {"symbol":"GOOG","price":"600.00"}
   */
  post("/saveJsonStock") {
    // get the POST request data
    val jsonString = request.body

    // needed for Lift-JSON
    implicit val formats = DefaultFormats
    // convert the JSON string to a JValue object
    val jValue = parse(jsonString)
    // deserialize the string into a Stock object
    val stock = jValue.extract[Stock]
    // for debugging
    println(stock)
    // you can send information back to the client in the response header
    response.addHeader("ACK", "GOT IT")
  }
}

// a simple Stock class
class Stock(var symbol: String, var price: Double) {
  override def toString = symbol + ", " + price
}

class BondServlet extends ScalatraServlet {
  get("/") {
    <p>Hello from BondServlet</p>
  }
}
