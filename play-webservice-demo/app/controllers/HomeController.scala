package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import models.Stock

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getStock = Action {
    val stock = Stock("GOOG", 650.0)
    Ok(Json.toJson(stock))
  }

  def saveStock = Action { request =>
    val json = request.body.asJson.get
    val stock = json.as[Stock]
    println(stock)
    Ok
  }
}
