package controllers

import javax.inject.Inject
import javax.inject.Singleton
import models.CarAdFormats.carAdFormat
import models.CarAdTypes.CarAd
import play.api.libs.json.JsError
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import play.api.mvc.Controller
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

  def getCarAds(sort: Option[String]) = Action { request =>
    val carAds = if (sort.isDefined) carAdService.getCarAds(sort.get) else carAdService.getCarAds()
    Ok(Json.toJson(carAds))
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