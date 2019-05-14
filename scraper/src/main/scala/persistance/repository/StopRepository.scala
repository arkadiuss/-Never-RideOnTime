package persistance.repository

import models.Stop
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters.equal
import persistance.Database

import scala.concurrent.Future

class StopRepository() extends Repository [Stop] {
  override val collection: MongoCollection[Stop] = Database.db().getCollection("stops")
  override def insert(obj: Stop): Future[_] = collection.insertOne(obj).toFuture()
  def insertMany(obj: Seq[Stop]): Future[_] = collection.insertMany(obj).toFuture()
  override def findById(id: Int) = collection.find(equal("_id", id)).first().head()
  def findAll() = collection.find().toFuture()
}
