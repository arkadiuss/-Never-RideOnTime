package api.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Vehicle
import spray.json.DefaultJsonProtocol

final case class VehiclesResponse(
                    lastUpdate: Long,
                    vehicles: List[Vehicle]
                  )

case object VehiclesResponse extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val vehiclesResponseFormat = jsonFormat2(VehiclesResponse.apply)
}
