package services

import scala.collection.JavaConversions._
import scala.util.Random

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.Table

import javax.inject.Inject
import javax.inject.Singleton
import play.Configuration

@Singleton
class CarService @Inject() (configuration: Configuration) {

  private val cars = new DynamoDB(new AmazonDynamoDBClient().withRegion(Regions.US_EAST_1).asInstanceOf[AmazonDynamoDBClient])
                              .getTable("CarsTest")

  def createCar() {
    cars.putItem(new Item()
                 .withPrimaryKey("id", Random.nextPrintableChar + "")
                 .withString("title", "aaaaaaaaa"))
  }
  
  def getCars(): Seq[String] = {
    val returnedCars = Nil
//    cars.scan().foreach((car: Item) => println(3))
    cars.scan().map(_.toJSON).to[collection.immutable.Seq]
//    superheroesRetrieved.stream().map(Item::toJSON).collect(Collectors.toList());
  }
}