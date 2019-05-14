package api.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Passage
import spray.json.DefaultJsonProtocol

final case class StopInfoResponse(
                                 actual: List[Passage],
                                 old: List[Passage]
                                 )

case object StopInfoResponse extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopInfoResponseFormat = jsonFormat2(StopInfoResponse.apply)
}
