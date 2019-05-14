package api.responses

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.Passage
import spray.json.DefaultJsonProtocol

final case class StopInfoResponse(
                                   actual: List[ApiPassage],
                                   old: List[ApiPassage],
                                   stopShortName: String
                                 )

case object StopInfoResponse extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopInfoResponseFormat = jsonFormat3(StopInfoResponse.apply)
}
