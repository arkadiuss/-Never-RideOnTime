package api

import akka.actor.{Actor, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import models.Stop
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ApiClient {
  def props: Props = Props(new ApiClient)
}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val stopFormat = jsonFormat6(Stop)
  implicit val stopResponseFormat = jsonFormat1(StopResponse)
}

class ApiClient extends Actor with JsonSupport {
  final val baseUrl = "http://91.223.13.70/internetservice/"
  final val http = Http(context.system)
  final implicit val materializer = ActorMaterializer()
  final implicit val executionContext = context.system.dispatcher

  def get(r: Request): Unit = {
    val responseFuture: Future[HttpResponse] = http.singleRequest(HttpRequest(uri = baseUrl + r.url))
    responseFuture.flatMap(res => Unmarshal(res.entity).to[StopResponse]).onComplete {
      case Success(res) =>
        println("DUPA" + res)
      case Failure(exception) => println(exception)
    }
  }

  def parseResponse(res: HttpResponse): Unit = {
  }

  override def receive: Receive = {
    case r: Request => get(r)
  }
}

