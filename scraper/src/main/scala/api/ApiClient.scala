package api

import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import api.responses.StopResponse

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ApiClient {
  def props: Props = Props(new ApiClient)
}

class ApiClient extends Actor {
  final val baseUrl = "http://91.223.13.70/internetservice/"
  final val http = Http(context.system)
  final implicit val materializer = ActorMaterializer()
  final implicit val executionContext = context.system.dispatcher
  private val apiResponseHandler = context.actorOf(ApiResponseHandler.props, "apiResponseHandler")

  def get[T](r: Request[T]): Unit = {
    val responseFuture: Future[HttpResponse] = http.singleRequest(HttpRequest(uri = baseUrl + r.url))
    responseFuture.flatMap(res => r.map(res.entity)).onComplete {
      case Success(res) => apiResponseHandler ! res
      case Failure(exception) => println(exception)
    }
  }

  override def receive: Receive = {
    case r: Request[_] => get(r)
  }
}
