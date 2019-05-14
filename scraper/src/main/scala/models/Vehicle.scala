package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Vehicle(
                    id: String,
                    heading: Option[Int],
                    category: Option[String],
                    longitude: Option[Int],
                    latitude: Option[Int],
                    tripId: Option[String],
                    name: Option[String],
                    isDeleted: Option[Boolean]
                  )

case object Vehicle extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val vehicleFormat = jsonFormat8(Vehicle.apply)
}