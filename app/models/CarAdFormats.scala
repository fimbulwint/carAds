package models

import models.CarAdTypes.CarAd
import models.CarAdTypes.Diesel
import models.CarAdTypes.FuelType
import models.CarAdTypes.Gasoline
import models.CarAdTypes.NewCarAd
import models.CarAdTypes.UsedCarAd
import play.api.data.validation.ValidationError
import play.api.libs.json.Format
import play.api.libs.json.JsPath
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.libs.json.Writes.dateWrites

object CarAdFormats {
  implicit val fuelTypeReads = this.getFuelTypeReads
  implicit val fuelTypeWrites = this.getFuelTypeWrites
  implicit val iso8601DateWrites = dateWrites("yyyy-MM-dd")

  implicit val newCarAdFormat = Json.format[NewCarAd]
  implicit val usedCarAdFormat = Json.format[UsedCarAd]

  implicit val carAdFormat = new Format[CarAd] {

    def writes(ad: CarAd) = ad match {
      case newAd: NewCarAd   => Json.toJson(newAd)
      case usedAd: UsedCarAd => Json.toJson(usedAd)
    }

    def reads(json: JsValue) = {
      val isNew = (json \ "new").validate[Boolean].asOpt.get
      if (isNew) json.validate[NewCarAd] else json.validate[UsedCarAd]
    }

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