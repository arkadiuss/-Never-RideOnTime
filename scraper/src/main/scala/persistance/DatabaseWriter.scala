package persistance

import akka.actor.{Actor, Props}
import models.{Stop, Vehicle}

object DatabaseWriter {
  def props: Props = Props(new DatabaseWriter)
}

class DatabaseWriter extends Actor{
  private val stopRepository = Database.stopRepository()
  private val vehiclesRepository = Database.vehiclesRepository()

  private def saveStops(stops: Seq[Stop]): Unit = {
    println("Inserting stops to database")
    stopRepository.insertMany(stops)
  }

  private def saveVehicles(vehicles: Seq[Vehicle]): Unit = {
    println("Inserting vehicles to database "+vehicles)
    vehiclesRepository.insertMany(vehicles)
  }

  override def receive: Receive = {
    case req: SaveStopsRequest => saveStops(req.stops)
    case req: SaveVehiclesRequest => saveVehicles(req.vehicle)
  }
}


sealed case class SaveRequest()
case class SaveStopsRequest(stops: Seq[Stop])
case class SaveVehiclesRequest(vehicle: Seq[Vehicle])