import akka.actor.ActorSystem
import api.{StopRequest, StopsRequest, VehiclesRequest}
import persistance.Database
import scheduling.{RecurringTask, Scheduler, Task}

import scala.concurrent.duration._
import scala.util.Success

object Main extends App {
  val system = ActorSystem("scraperSystem")
  val scheduler = system.actorOf(Scheduler.props, "scheduler")
  implicit val executor = system.dispatcher
  Database.stopRepository().findAll()
      .andThen{ case Success(stops) =>
        if(stops.isEmpty) {
          println("No stops. Downloading new")
          scheduler ! Task(new StopsRequest)
          scheduler ! Task(new VehiclesRequest)
        } else {
          println("Stops already exist")
        }
      }
  println("Waiting for stops")
  Thread.sleep(30000)
  Database.stopRepository().findAll()
      .andThen { case Success(stops) =>
        println("There are " + stops.size + "stops")
        stops.foreach(stop => {
          scheduler ! RecurringTask(s"stop${stop.shortName}", new StopRequest(stop.shortName), 1 minutes)
          Thread.sleep(500)
        })
      }
}
