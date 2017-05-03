package services

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute

@DynamoDBTable(tableName = "CarAds")
class NewCarAdDto() extends CarAdDto {

  @DynamoDBHashKey(attributeName = "id")
  def getId = id
  def setId(id: Int) = this.id = id

  def getTitle = title
  def setTitle(title: String) = this.title = title

  def getFuel = fuel
  def setFuel(fuel: String) = this.fuel = fuel

  def getPrice = price
  def setPrice(price: Int) = this.price = price

  @DynamoDBAttribute(attributeName="isNew")  
  def getNew = true
  def setNew(`new`: Boolean) = this.`new` = true
}