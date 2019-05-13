package api

import akka.actor.{Actor, Props}
import api.responses.StopResponse
import persistance.{DatabaseWriter, SaveStopsRequest}

object ApiResponseHandler {
  def props: Props = Props(new ApiResponseHandler)
}

class ApiResponseHandler extends Actor {
  private val databaseWriter = context.actorOf(DatabaseWriter.props, "databaseWriter")

  override def receive: Receive = {
    case res: StopResponse => databaseWriter ! SaveStopsRequest(res.stops)
    case res: Any => println("Unknown response" + res)
  }
}
