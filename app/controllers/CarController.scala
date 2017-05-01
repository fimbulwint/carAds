package controllers

import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.Action
import play.api.mvc.Controller
import services.CarService

import play.api.libs.json._

@Singleton
class CarController @Inject() (carService: CarService) extends Controller {

  def createCar() = Action { request =>
    carService.createCar()
    Ok("AAA")
  }
  
  def getCars() = Action { request =>
    Ok(Json.toJson(carService.getCars()))
  }
}