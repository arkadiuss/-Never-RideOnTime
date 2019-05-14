package api

import akka.actor.{Actor, Props}
import api.responses.{StopInfoResponse, StopResponse, VehiclesResponse}
import com.typesafe.scalalogging.Logger
import persistance.{DatabaseWriter, SavePassagesRequest, SaveStopsRequest, SaveVehiclesRequest}

object ApiResponseHandler {
  def props: Props = Props(new ApiResponseHandler)
}

class ApiResponseHandler extends Actor {
  private val databaseWriter = context.actorOf(DatabaseWriter.props, "databaseWriter")

  private val logger = Logger[ApiResponseHandler]

  override def receive: Receive = {
    case res: StopResponse => databaseWriter ! SaveStopsRequest(res.stops)
    case res: VehiclesResponse => databaseWriter ! SaveVehiclesRequest(res.vehicles)
    case res: StopInfoResponse => handleStopInfoResponse(res)
    case res: Any => logger.warn("Unknown response" + res)
  }

  private def handleStopInfoResponse(stopInfo: StopInfoResponse): Unit = {
    def departures = stopInfo.actual ++ stopInfo.old
    //TODO: filtering and mapping passages
    databaseWriter ! SavePassagesRequest(departures)
  }
}
