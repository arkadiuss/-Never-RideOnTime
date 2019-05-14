package persistance.repository

import models.Passage
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters.equal
import persistance.Database

import scala.concurrent.Future

class PassagesRepository() extends Repository [Passage] {
  override val collection: MongoCollection[Passage] = Database.db().getCollection("passages")
  override def insert(obj: Passage): Future[_] = collection.insertOne(obj).toFuture()
  def insertMany(obj: Seq[Passage]): Future[_] = collection.insertMany(obj).toFuture()
  override def findById(id: Int) = collection.find(equal("_id", id)).first().head()
  def findAll() = collection.find().toFuture()
}
