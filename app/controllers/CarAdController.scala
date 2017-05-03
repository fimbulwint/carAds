package controllers

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import javax.inject.Inject
import javax.inject.Singleton
import models.CarAdTypes.CarAd
import models.CarAdTypes.Diesel
import models.CarAdTypes.FuelType
import models.CarAdTypes.Gasoline
import models.CarAdTypes.NewCarAd
import models.CarAdTypes.UsedCarAd
import play.api.data.validation.ValidationError
import play.api.libs.json.JsDefined
import play.api.libs.json.JsError
import play.api.libs.json.JsPath
import play.api.libs.json.JsString
import play.api.libs.json.JsUndefined
import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.libs.json.Writes.dateWrites
import play.api.mvc.Action
import play.api.mvc.BodyParsers
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Result
import services.CarAdService

@Singleton
class CarAdController @Inject() (carAdService: CarAdService) extends Controller {

  implicit val fuelTypeReads = this.getFuelTypeReads
  implicit val fuelTypeWrites = this.getFuelTypeWrites
  implicit val iso8601DateWrites = dateWrites("yyyy-MM-dd")

  implicit val newCarAdFormat = Json.format[NewCarAd]
  implicit val usedCarAdFormat = Json.format[UsedCarAd]

  implicit val carAdWrites = new Writes[CarAd] {
    def writes(ad: CarAd) = ad match {
      case newAd: NewCarAd   => Json.toJson(newAd)
      case usedAd: UsedCarAd => Json.toJson(usedAd)
    }
  }

  def createCarAd() = Action(BodyParsers.parse.json) { request =>
    val isCarNew = request.body \ "new"
    isCarNew match {
      case JsDefined(isCarNew) => {
        createAd(request, isCarNew)
      }
      case undefined: JsUndefined => BadRequest("Field 'new' is required")
    }
  }

  def getCarAds() = Action { request =>
    Ok(Json.toJson(carAdService.getCarAds()))
  }

  private def createAd(request: Request[JsValue], isCarNew: JsValue): Result = {
    Try(isCarNew.as[Boolean]) match {
      case Success(isCarNew) => {
        if (isCarNew) {
          createNewCarAd(request)
        } else {
          createUsedCarAd(request)
        }
      }
      case Failure(e) => {
        BadRequest("Field 'new' should be a boolean")
      }
    }
  }

  private def createNewCarAd(request: Request[JsValue]): Result = {
    request.body.validate[NewCarAd].fold(
      validationErrors => {
        BadRequest(JsError.toJson(validationErrors))
      },
      newCarAd => {
        carAdService.createCarAd(newCarAd)
        Ok("New car ad created successfully")
      })
  }

  private def createUsedCarAd(request: Request[JsValue]): Result = {
    request.body.validate[UsedCarAd].fold(
      validationErrors => {
        BadRequest(JsError.toJson(validationErrors))
      },
      usedCarAd => {
        carAdService.createCarAd(usedCarAd)
        Ok("Used car ad created successfully")
      })
  }

  private def getFuelTypeReads(): Reads[FuelType] = {
    (JsPath).read[String]
            .map(this.readFuelType)
            .filter(
              ValidationError("Fuel type not recognized")) { _.isDefined }
            .map(_.get)
  }

  private def readFuelType(fuelType: String): Option[FuelType] = {
    List(Gasoline, Diesel).find(_.toString.toLowerCase == fuelType.toLowerCase)
  }

  private def getFuelTypeWrites(): Writes[FuelType] = {
    new Writes[FuelType] {
      def writes(fuel: FuelType): JsString = {
        new JsString(fuel.toString.toLowerCase)
      }
    }
  }
}