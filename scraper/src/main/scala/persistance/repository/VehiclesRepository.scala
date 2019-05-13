package persistance.repository

import models.Vehicle
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters.equal
import persistance.Database

import scala.concurrent.Future

class VehiclesRepository() extends Repository [Vehicle] {
  override val collection: MongoCollection[Vehicle] = Database.db().getCollection("vehicles")
  override def insert(obj: Vehicle): Future[_] = collection.insertOne(obj).toFuture()
  def insertMany(obj: Seq[Vehicle]): Future[_] = collection.insertMany(obj).toFuture()
  override def findById(id: Int) = collection.find(equal("_id", id)).first().head()
  def findAll() = collection.find().toFuture()
}
