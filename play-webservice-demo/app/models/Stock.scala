package models

case class Stock(symbol: String, price: Double)
object Stock {
  import play.api.libs.json._

  implicit object StockFormat extends Format[Stock] {
    // convert from JSON string to a Stock object (de-serializing from JSON)
    def reads(json: JsValue): JsResult[Stock] = {
      val symbol = (json \ "symbol").as[String]
      val price = (json \ "price").as[Double]
      JsSuccess(Stock(symbol, price))
    }

    // convert from Stock object to JSON (serializing to JSON)
    def writes(s: Stock): JsValue = {
      // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
      val stockAsList = Seq("symbol" -> JsString(s.symbol), "price" -> JsNumber(s.price))
      JsObject(stockAsList)
    }
  }
}
