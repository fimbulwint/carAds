package services

import java.text.SimpleDateFormat
import java.util.Date

import scala.collection.JavaConversions._

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue

import javax.inject.Singleton
import models.CarAdTypes.CarAd
import models.CarAdTypes.Diesel
import models.CarAdTypes.FuelType
import models.CarAdTypes.Gasoline
import models.CarAdTypes.NewCarAd
import models.CarAdTypes.UsedCarAd

@Singleton
class CarAdService {

  private val dynamoMapper = new DynamoDBMapper(new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1))

  def createCarAd(newCarAd: NewCarAd) {
    val newCarAdDto = new NewCarAdDto()
    newCarAdDto.setId(newCarAd.id)
    newCarAdDto.setTitle(newCarAd.title)
    newCarAdDto.setFuel(newCarAd.fuel.toString.toLowerCase)
    newCarAdDto.setPrice(newCarAd.price)
    newCarAdDto.setNew(newCarAd.`new`)
    dynamoMapper.save(newCarAdDto)
  }
  
  def createCarAd(usedCarAd: UsedCarAd) {
    val usedCarAdDto = new UsedCarAdDto()
    usedCarAdDto.setId(usedCarAd.id)
    usedCarAdDto.setTitle(usedCarAd.title)
    usedCarAdDto.setFuel(usedCarAd.fuel.toString.toLowerCase)
    usedCarAdDto.setPrice(usedCarAd.price)
    usedCarAdDto.setNew(usedCarAd.`new`)
    usedCarAdDto.setMileage(usedCarAd.mileage)
    usedCarAdDto.setFirstReg(new SimpleDateFormat("yyyy-MM-dd").format(usedCarAd.firstReg))
    dynamoMapper.save(usedCarAdDto)
  }
  
  def getCarAds(): Seq[CarAd] = {
    val newAds = getCarAds(classOf[NewCarAdDto], Map(":isNew" -> new AttributeValue().withN("1"))).map(map(_))
    val usedAds = getCarAds(classOf[UsedCarAdDto], Map(":isNew" -> new AttributeValue().withN("0"))).map(map(_))
    newAds ++ usedAds
  }
  
  private def getCarAds[AdType <: CarAdDto](carAdType: Class[AdType], filter: Map[String, AttributeValue]): Seq[AdType] = {
    dynamoMapper.scan(carAdType, new DynamoDBScanExpression()
                               .withFilterExpression("isNew = :isNew")
                               .withExpressionAttributeValues(filter))
  }

  private def map(carAdDto: NewCarAdDto): NewCarAd = {
    new NewCarAd(carAdDto.getId, carAdDto.getTitle, carAdDto.getFuel match {
      case "gasoline" => Gasoline
      case "diesel" => Diesel
    }, carAdDto.getPrice, carAdDto.getNew)
  }
  
  private def map(carAdDto: UsedCarAdDto): UsedCarAd = {
    new UsedCarAd(carAdDto.getId, carAdDto.getTitle, carAdDto.getFuel match {
      case "gasoline" => Gasoline
      case "diesel" => Diesel
    }, carAdDto.getPrice, carAdDto.getNew, carAdDto.getMileage, new SimpleDateFormat("yyyy-MM-dd").parse(carAdDto.getFirstReg))
  }
}
