package api

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import api.responses.StopResponse

import scala.concurrent.Future

abstract sealed case class Request[T](url: String){
  def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[T]
}

//class StopRequest(stopID: String) extends Request(s"services/passageInfo/stopPassages/stop?stop=$stopID")
class StopsRequest extends Request[StopResponse](s"geoserviceDispatcher/services/stopinfo/stops?left=-648000000&bottom=-324000000&right=648000000&top=324000000") {
  override def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[StopResponse] = Unmarshal(obj).to[StopResponse]
}
