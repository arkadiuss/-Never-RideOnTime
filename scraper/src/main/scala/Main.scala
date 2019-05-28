import java.io.File

import akka.actor.ActorSystem
import api.{StopRequest, StopsRequest, VehiclesRequest}
import ch.qos.logback.classic.{Level, Logger}
import org.slf4j.LoggerFactory
import persistance.Database
import scheduling.{RecurringTask, Scheduler, Task}

import scala.concurrent.duration._
import scala.io.Source
import scala.util.Success

object Main extends App {
  val root = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
  root.setLevel(Level.INFO)

  val system = ActorSystem("scraperSystem")
  val scheduler = system.actorOf(Scheduler.props, "scheduler")
  implicit val executor = system.dispatcher

  println((new File("")).getAbsolutePath)
  val goodStops = Source.fromFile("/code/goodStops.txt").mkString.split(",")
  Database.stopRepository().findAll()
    .andThen { case Success(stops) =>
      if (stops.isEmpty) {
        root.info("No stops. Downloading new")
        scheduler ! Task(new StopsRequest)
        scheduler ! Task(new VehiclesRequest)
      } else {
        root.info("Stops already exist")
      }
    }

  root.info("Waiting for stops")
  Thread.sleep(30000)


  val stopRequestDelay = 1

  println(s"There are ${goodStops.size} stops")
  goodStops.foreach(stopShortName => {
    scheduler ! RecurringTask(s"stop $stopShortName", new StopRequest(stopShortName), 1 minutes)
    Thread.sleep(1000 * 60 * stopRequestDelay / goodStops.size)
  })
//  Database.stopRepository().findAll()
//    .andThen { case Success(stops) =>
//      root.info("There are " + stops.size + "stops")
//      stops.foreach(stop => {
//        scheduler ! RecurringTask(s"stop${stop.shortName}", new StopRequest(stop.shortName), 1 minutes)
//        Thread.sleep(500)
//      })
//    }
}
