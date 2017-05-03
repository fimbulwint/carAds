package models

import java.util.Date

object CarAdTypes {

  abstract class CarAd(id: Int, title: String, fuel: FuelType, price: Int, `new`: Boolean) {
    def getId = id
    def getTitle = title
    def getFuel = fuel
    def getPrice = price
    def isNew = `new`
  }
  case class NewCarAd(id: Int, title: String, fuel: FuelType, price: Int, `new`: Boolean) extends CarAd(id, title, fuel, price, `new`)
  case class UsedCarAd(id: Int, title: String, fuel: FuelType, price: Int, `new`: Boolean, mileage: Int, firstReg: Date) extends CarAd(id, title, fuel, price, `new`)

  sealed trait FuelType
  case object Gasoline extends FuelType
  case object Diesel extends FuelType

}
