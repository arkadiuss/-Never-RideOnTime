import models.Stop
import persistance.Database

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
  val stops = Database.stopRepository()
  stops.insert(new Stop("Miasteczko"))
  stops.insert(new Stop("Miasteczko2"))

  val getted = Await.result(stops.findAll(), Duration.Inf)
  for(s <- getted){
    print(s.name)
  }
}
