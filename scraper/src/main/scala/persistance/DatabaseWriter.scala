package persistance

import akka.actor.{Actor, Props}
import models.Stop

sealed case class WriteRequest()
case class SaveStopsRequest(stops: Seq[Stop])

object DatabaseWriter {
  def props: Props = Props(new DatabaseWriter)
}

class DatabaseWriter extends Actor{
  private val stopRepository = Database.stopRepository()

  private def saveStops(stops: Seq[Stop]): Unit = {
    println("Inserting stops to database")
    stopRepository.insertMany(stops)
  }

  override def receive: Receive = {
    case req: SaveStopsRequest => saveStops(req.stops)
  }
}
