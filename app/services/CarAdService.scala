package services

import scala.collection.JavaConversions.iterableAsScalaIterable

import com.amazonaws.services.dynamodbv2.document.Item

import javax.inject.Inject
import javax.inject.Singleton
import models.CarAdFormats.carAdFormat
import models.CarAdTypes.CarAd
import models.CarAdTypes.Diesel
import play.api.libs.json.Json

@Singleton
class CarAdService @Inject() (dynamoService: DynamoService) {
  
  private final val carAds = dynamoService.getCarAdsTable

  def createCarAd(carAd: CarAd) {
    carAds.putItem(Item.fromJSON(Json.toJson(carAd).toString))
  }
  
  def getCarAds(sortField: String = "id"): Seq[CarAd] = {
    carAds.scan().map(_.toJSON).map(Json.parse(_)).map(Json.fromJson(_)).map(_.asOpt)
          .filter(_.isDefined).map(_.get).to[Seq].sortWith(selectSortingFunction(sortField))
  }
  
  def getCarAd(id: Int): Option[CarAd] = {
    val carAd = carAds.getItem("id", id)
    if (carAd != null) Json.fromJson(Json.parse(carAd.toJSON)).asOpt else None
  }
  
  def deleteCarAd(id: Int): Boolean = {
    // Workaround, for some reason the deleteItem call always returns an empty item, whether it existed or not
    val carAd = carAds.getItem("id", id)
    carAds.deleteItem("id", id)
    return carAd != null
  }
  
  private def selectSortingFunction(sortField: String): (CarAd, CarAd) => Boolean = {
    (ad1: CarAd, ad2: CarAd) => {
      sortField match {
        case "id" => ad1.getId < ad2.getId
        case "title" => ad1.getTitle < ad2.getTitle
        case "fuel" => ad1.getFuel == Diesel
        case "price" => ad1.getPrice < ad2.getPrice
        case "new" => ad1.isNew < ad2.isNew
        case _ => ad1.getId < ad2.getId
      }
    }
  }
}
