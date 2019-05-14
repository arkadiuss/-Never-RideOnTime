package api

import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import api.responses.{StopInfoResponse, StopResponse, VehiclesResponse}

import scala.concurrent.Future

abstract sealed case class Request[T](url: String){
  def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[T]
}

class StopRequest(stopID: String) extends Request[StopInfoResponse](s"services/passageInfo/stopPassages/stop?stop=$stopID") {
  override def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[StopInfoResponse] = Unmarshal(obj).to[StopInfoResponse]
}

class StopsRequest extends Request[StopResponse](s"geoserviceDispatcher/services/stopinfo/stops?left=-648000000&bottom=-324000000&right=648000000&top=324000000") {
  override def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[StopResponse] = Unmarshal(obj).to[StopResponse]
}

class VehiclesRequest extends Request[VehiclesResponse](s"geoserviceDispatcher/services/vehicleinfo/vehicles") {
  override def map(obj: ResponseEntity)(implicit materializer: Materializer): Future[VehiclesResponse] = Unmarshal(obj).to[VehiclesResponse]
}

