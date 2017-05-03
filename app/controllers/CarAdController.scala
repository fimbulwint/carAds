package controllers

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import javax.inject.Inject
import javax.inject.Singleton
import models.CarAdFormats.carAdFormat
import models.CarAdFormats.newCarAdFormat
import models.CarAdFormats.usedCarAdFormat
import models.CarAdTypes.CarAd
import models.CarAdTypes.NewCarAd
import models.CarAdTypes.UsedCarAd
import play.api.libs.json.JsError
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Result
import services.CarAdService

@Singleton
class CarAdController @Inject() (carAdService: CarAdService) extends Controller {

  def createCarAd() = Action(BodyParsers.parse.json) { request =>
    request.body.validate[CarAd].fold(
      validationErrors => {
        BadRequest(JsError.toJson(validationErrors))
      },
      carAd => {
        carAdService.createCarAd(carAd)
        Ok("New car ad created successfully")
      })
  }

  def getCarAds() = Action { request =>
    Ok(Json.toJson(carAdService.getCarAds()))
  }

  def getCarAd(id: Int) = Action {
    carAdService.getCarAd(id) match {
      case Some(carAd) => Ok(Json.toJson(carAd))
      case None => NotFound
    }
  }

  def deleteCarAd(id: Int) = Action {
    if (carAdService.deleteCarAd(id)) Ok("Ad successfully deleted") else NotFound
  }
}