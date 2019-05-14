package api

import akka.actor.{Actor, Props}
import api.responses.{StopInfoResponse, StopResponse, VehiclesResponse}
import models.Passage
import com.typesafe.scalalogging.Logger
import persistance.{DatabaseWriter, SavePassagesRequest, SaveStopsRequest, SaveVehiclesRequest}

object ApiResponseHandler {
  def props: Props = Props(new ApiResponseHandler)
}

class ApiResponseHandler extends Actor {
  private val databaseWriter = context.actorOf(DatabaseWriter.props, "databaseWriter")

  private val logger = Logger[ApiResponseHandler]

  private def handleStopInfoResponse(stopInfo: StopInfoResponse): Unit = {
    val apiPassages = stopInfo.actual ++ stopInfo.old
    val passages = apiPassages.map( it => {
      Passage(
        it.actualRelativeTime,
        it.actualTime,
        it.plannedTime,
        it.status,
        it.patternText,
        it.routeId,
        it.tripId,
        it.passageid,
        stopInfo.stopShortName,
        System.currentTimeMillis()
      )
    })
      .filterNot(it => it.status == "PLANNED")
      .filter(it => Math.abs(it.actualRelativeTime) < 300)
    databaseWriter ! SavePassagesRequest(passages)
  }

  override def receive: Receive = {
    case res: StopResponse => databaseWriter ! SaveStopsRequest(res.stops)
    case res: VehiclesResponse => databaseWriter ! SaveVehiclesRequest(res.vehicles)
    case res: StopInfoResponse => handleStopInfoResponse(res)
    case res: Any => logger.warn("Unknown response" + res)
  }
}
