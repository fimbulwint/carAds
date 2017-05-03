package services

import scala.collection.JavaConversions.iterableAsScalaIterable
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import javax.inject.Singleton
import models.CarAdFormats.carAdFormat
import models.CarAdTypes.CarAd
import models.CarAdTypes.Diesel
import play.api.libs.json.Json
import com.amazonaws.services.dynamodbv2.document.Table
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput

@Singleton
class CarAdService {
  
  private final val dynamoClient: AmazonDynamoDBClient = new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1)
  private final val carAds = getCarAdsTable

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
  
  private def getCarAdsTable(): Table = {
    Try(dynamoClient.describeTable(CarAdService.CAR_ADS_TABLE_NAME)) match {
      case Success(_) => new DynamoDB(dynamoClient).getTable(CarAdService.CAR_ADS_TABLE_NAME)
      case Failure(_) => createCarAdsTable
    }
  }
  
  private def createCarAdsTable(): Table = {
    val primaryKey = new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH)
    val attrsDef = new AttributeDefinition().withAttributeName("id").withAttributeType("N")
    val throughput = new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L)
    val request = new CreateTableRequest().withTableName(CarAdService.CAR_ADS_TABLE_NAME)
                                          .withKeySchema(primaryKey)
                                          .withAttributeDefinitions(attrsDef)
                                          .withProvisionedThroughput(throughput);
    val table = new DynamoDB(dynamoClient).createTable(request)
    table.waitForActive()
    table
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

object CarAdService {
  private final val CAR_ADS_TABLE_NAME = "CarAds"
}
