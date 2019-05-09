import akka.actor.ActorSystem
import scheduling.SchedulingActor

object Main extends App {
  val system = ActorSystem("scraperSystem")
  val scheduler = system.actorOf(SchedulingActor.props, "scheduler")

  scheduler ! "start"
}
