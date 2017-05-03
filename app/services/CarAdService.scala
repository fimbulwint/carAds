package services

import scala.collection.JavaConversions.iterableAsScalaIterable
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import javax.inject.Singleton
import models.CarAdFormats.carAdFormat
import models.CarAdTypes.CarAd
import play.api.libs.json.Json
import com.amazonaws.services.dynamodbv2.document.PrimaryKey

@Singleton
class CarAdService {
  
  private final val dynamoClient: AmazonDynamoDBClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1)
  private final val carAds = new DynamoDB(dynamoClient).getTable("CarAds")

  def createCarAd(carAd: CarAd) {
    carAds.putItem(Item.fromJSON(Json.toJson(carAd).toString))
  }
  def getCarAds(): Seq[CarAd] = {
    carAds.scan().map(_.toJSON).map(Json.parse(_)).map(Json.fromJson(_)).map(_.asOpt.get).to[Seq]
  }
  
  def getCarAd(id: Int): Option[CarAd] = {
    val carAd = carAds.getItem("id", id)
    if (carAd != null) Json.fromJson(Json.parse(carAd.toJSON)).asOpt else None
  }
  
  def deleteCarAd(id: Int): Boolean = {
    // Workaround, for some reason the deleteItem call always returns an empty item, whether it existed or now
    val carAd = carAds.getItem("id", id)
    carAds.deleteItem("id", id)
    return carAd != null
  }
}
