package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class Passage(
                    actualRelativeTime: Long,
                    actualTime: Option[String],
                    plannedTime: String,
                    status: String,
                    patternText: String,
                    routeId: String,
                    tripId: String,
                    passageid: String
                    )


case object Passage extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopFormat = jsonFormat8(Passage.apply)
}
