package api.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Stop
import spray.json.DefaultJsonProtocol

final case class StopResponse(stops: List[Stop])

case object StopResponse extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopResponseFormat = jsonFormat1(StopResponse.apply)
}
