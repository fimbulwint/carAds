package services

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute

@DynamoDBTable(tableName = "CarAds")
class UsedCarAdDto() extends CarAdDto {
  var mileage: Int = _
  var firstReg: String = _

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
  def getNew = false
  def setNew(`new`: Boolean) = this.`new` = false

  def getMileage = mileage
  def setMileage(mileage: Int) = this.mileage = mileage

  def getFirstReg = firstReg
  def setFirstReg(firstReg: String) = this.firstReg = firstReg
}