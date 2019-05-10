import akka.actor.ActorSystem
import scheduling.SchedulingActor
import api.{ApiClient, StopsRequest}

object Main extends App {
  val system = ActorSystem("scraperSystem")
  val scheduler = system.actorOf(SchedulingActor.props, "scheduler")

  val apiClient = system.actorOf(ApiClient.props, name="api_client")

  scheduler ! "start"

  apiClient ! new StopsRequest
}
