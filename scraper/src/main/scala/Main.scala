import akka.actor.ActorSystem
import api.{StopsRequest, VehiclesRequest}
import scheduling.{RecurringTask, Scheduler, Task}

import scala.concurrent.duration._

object Main extends App {
  val system = ActorSystem("scraperSystem")
  val scheduler = system.actorOf(Scheduler.props, "scheduler")
  //scheduler ! RecurringTask("stops", new StopsRequest, 10 seconds)
  scheduler ! Task(new StopsRequest)
  scheduler ! Task(new VehiclesRequest)
}
