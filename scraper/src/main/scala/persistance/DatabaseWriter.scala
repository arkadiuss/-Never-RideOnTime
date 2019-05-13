package persistance

import akka.actor.{Actor, Props}
import models.{Passage, Stop, Vehicle}

object DatabaseWriter {
  def props: Props = Props(new DatabaseWriter)
}

class DatabaseWriter extends Actor{
  private val stopRepository = Database.stopRepository()
  private val vehiclesRepository = Database.vehiclesRepository()
  private val passagesRepository = Database.passagesRepository()

  private def saveStops(stops: Seq[Stop]): Unit = {
    println("Inserting stops to database")
    stopRepository.insertMany(stops)
  }

  private def saveVehicles(vehicles: Seq[Vehicle]): Unit = {
    println("Inserting vehicles to database")
    vehiclesRepository.insertMany(vehicles)
  }

  private def savePassages(passages: Seq[Passage]): Unit = {
    println("Inserting passages to database ")
    passagesRepository.insertMany(passages)
  }

  override def receive: Receive = {
    case req: SaveStopsRequest => saveStops(req.stops)
    case req: SaveVehiclesRequest => saveVehicles(req.vehicles)
    case res: SavePassagesRequest => savePassages(res.passages)
  }
}


sealed case class SaveRequest()
case class SaveStopsRequest(stops: Seq[Stop])
case class SaveVehiclesRequest(vehicles: Seq[Vehicle])
case class SavePassagesRequest(passages: Seq[Passage])