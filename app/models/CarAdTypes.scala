package models

import java.time.LocalDate

object CarAdTypes {

  case class NewCarAd(id: Int, title: String, fuel: String, price: Int, `new`: Boolean)
  case class UsedCarAd(id: Int, title: String, fuel: String, price: Int, `new`: Boolean, mileage: Int, firstReg: String)
  
  sealed trait FuelType
  case object Gasoline extends FuelType
  case object Diesel extends FuelType

}
