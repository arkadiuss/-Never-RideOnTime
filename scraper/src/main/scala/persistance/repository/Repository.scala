package persistance.repository

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters._

import scala.concurrent.Future
import scala.reflect.ClassTag

trait Repository[T] {
  protected val DEFAULT_LIMIT_SIZE = 50
  protected val collection: MongoCollection[T]

  def insert(obj: T): Future[_]
  def findById(id: Int): Future[T]

}