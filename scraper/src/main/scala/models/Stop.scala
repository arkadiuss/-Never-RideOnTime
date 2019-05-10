package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Stop(category: String, id: String, latitude: Int, longitude: Int, name: String, shortName: String)

case object Stop extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopFormat = jsonFormat6(Stop.apply)
}
