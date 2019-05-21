package api

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy, QueueOfferResult}
import com.typesafe.scalalogging.Logger

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}

object ApiClient {
  def props: Props = Props(new ApiClient)
}

class ApiClient extends Actor {
  val baseUrl = "http://91.223.13.70/internetservice/"
  val http = Http(context.system)

  val queueSize = 32

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = context.system.dispatcher
  private val apiResponseHandler = context.actorOf(ApiResponseHandler.props, "apiResponseHandler")

  val poolClientFlow = Http().newHostConnectionPool[Promise[HttpResponse]]("91.223.13.70")
  val queue =
    Source.queue[(HttpRequest, Promise[HttpResponse])](queueSize, OverflowStrategy.dropNew)
      .via(poolClientFlow)
      .toMat(Sink.foreach({
        case (Success(resp), p) => p.success(resp)
        case (Failure(e), p) => p.failure(e)
      }))(Keep.left)
      .run()

  private val logger = Logger[ApiClient]

  override def receive: Receive = {
    case r: Request[_] => get(r)
  }

  def queueRequest(request: HttpRequest): Future[HttpResponse] = {
    val responsePromise = Promise[HttpResponse]()
    queue.offer(request -> responsePromise).flatMap {
      case QueueOfferResult.Enqueued => responsePromise.future
      case QueueOfferResult.Dropped => Future.failed(new RuntimeException("Queue overflowed. Try again later."))
      case QueueOfferResult.Failure(ex) => Future.failed(ex)
      case QueueOfferResult.QueueClosed => Future.failed(new RuntimeException("Queue was closed (pool shut down) while running the request. Try again later."))
    }
  }

  def get[T](r: Request[T]): Unit = {
    val responseFuture: Future[HttpResponse] = queueRequest(HttpRequest(uri = baseUrl + r.url))
    responseFuture.flatMap(res => r.map(res.entity)).onComplete {
      case Success(res) => apiResponseHandler ! res
      case Failure(exception) => logger.warn(exception.getLocalizedMessage)
    }
  }
}
