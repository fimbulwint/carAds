package services

import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.util.Random

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item

import javax.inject.Singleton
import models.CarAdTypes.NewCarAd
import models.CarAdTypes.UsedCarAd

@Singleton
class CarAdService {
  
  private val carAds = new DynamoDB(new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1).asInstanceOf[AmazonDynamoDBClient])
                              .getTable("CarsTest")

  def createCarAd(newCarAd: NewCarAd) {
    carAds.putItem(new Item()
                 .withPrimaryKey(CarAdService.KEY_FIELD, Random.nextPrintableChar + "")
                 .withString(CarAdService.TITLE_FIELD, newCarAd.title)
                 .withString(CarAdService.FUEL_FIELD, newCarAd.fuel)
                 .withInt(CarAdService.PRICE_FIELD, newCarAd.price)
                 .withBoolean(CarAdService.NEW_FIELD, true))
  }
  
  def createCarAd(usedCarAd: UsedCarAd) {
    carAds.putItem(new Item()
                 .withPrimaryKey(CarAdService.KEY_FIELD, Random.nextPrintableChar + "")
                 .withString(CarAdService.TITLE_FIELD, usedCarAd.title)
                 .withString(CarAdService.FUEL_FIELD, usedCarAd.fuel)
                 .withInt(CarAdService.PRICE_FIELD, usedCarAd.price)
                 .withBoolean(CarAdService.NEW_FIELD, false)
                 .withInt(CarAdService.MILEAGE_FIELD, usedCarAd.mileage)
                 .withString(CarAdService.FIRST_REGISTRATION_FIELD, usedCarAd.firstReg))
  }
  
  def getCarAds(): Seq[String] = {
    carAds.scan().map(_.toJSON).to[collection.immutable.Seq]
  }
}

object CarAdService {
  final val KEY_FIELD = "id"
  final val TITLE_FIELD = "title"
  final val FUEL_FIELD = "fuel"
  final val PRICE_FIELD = "price"
  final val NEW_FIELD = "new"
  final val MILEAGE_FIELD = "mileage"
  final val FIRST_REGISTRATION_FIELD = "firstReg"
}