package api.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class ApiPassage(
                    actualRelativeTime: Long,
                    actualTime: Option[String],
                    plannedTime: String,
                    status: String,
                    patternText: String,
                    routeId: String,
                    tripId: String,
                    passageid: String
                    )


case object ApiPassage extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val apiPassageFormat = jsonFormat8(ApiPassage.apply)
}



