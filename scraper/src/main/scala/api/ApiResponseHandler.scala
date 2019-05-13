package api

import akka.actor.{Actor, Props}
import api.responses.{StopResponse, VehiclesResponse}
import persistance.{DatabaseWriter, SaveStopsRequest, SaveVehiclesRequest}

object ApiResponseHandler {
  def props: Props = Props(new ApiResponseHandler)
}

class ApiResponseHandler extends Actor {
  private val databaseWriter = context.actorOf(DatabaseWriter.props, "databaseWriter")

  override def receive: Receive = {
    case res: StopResponse => databaseWriter ! SaveStopsRequest(res.stops)
    case res: VehiclesResponse => databaseWriter ! SaveVehiclesRequest(res.vehicles)
    case res: Any => println("Unknown response" + res)
  }
}
