package models

import java.util.Date

object CarAdTypes {

  case class NewCarAd(id: Int, title: String, fuel: FuelType, price: Int, `new`: Boolean)
  case class UsedCarAd(id: Int, title: String, fuel: FuelType, price: Int, `new`: Boolean, mileage: Int, firstReg: Date)
  
  sealed trait FuelType
  case object Gasoline extends FuelType
  case object Diesel extends FuelType

}
