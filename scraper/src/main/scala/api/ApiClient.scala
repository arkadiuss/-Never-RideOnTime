package api

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ApiClient {
  def props: Props = Props(new ApiClient)
}

class ApiClient extends Actor {
  val baseUrl = "http://91.223.13.70/internetservice/"

  def get(r: Request): Unit = {
    implicit val system = ActorSystem()
    implicit val executionContext = system.dispatcher
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = baseUrl + r.url))
    responseFuture.onComplete {
      case Success(res) =>
        println(res)
        println("DUPA" + res.entity)
      case Failure(exception) => println(exception)
    }
  }

  override def receive: Receive = {
    case r: Request => get(r)
  }
}

