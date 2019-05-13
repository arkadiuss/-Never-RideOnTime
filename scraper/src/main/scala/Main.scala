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
  //scheduler ! RecurringTask("stops", new StopsRequest, 10 seconds)
  //scheduler ! Task(new StopsRequest)
  //scheduler ! Task(new VehiclesRequest)
  Database.stopRepository().findAll()
      .andThen { case Success(stops) =>
        println("There are " + stops.size + "stops")
        stops.foreach(stop => {
          scheduler ! Task(new StopRequest(stop.shortName))
          Thread.sleep(1000)
        })
      }
}
