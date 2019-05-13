package persistance

import org.mongodb.scala.{MongoClient, MongoDatabase}
import com.typesafe.config.{Config, ConfigFactory}
import models.{Passage, Stop, Vehicle}
import persistance.repository.{PassagesRepository, StopRepository, VehiclesRepository}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

object Database {
  private lazy val config: Config = ConfigFactory.load()
  private lazy val mongoClient: MongoClient = MongoClient(config.getString("mongo.uri"))
  private val codecRegistry = fromRegistries(fromProviders(classOf[Stop], classOf[Vehicle], classOf[Passage]), DEFAULT_CODEC_REGISTRY )
  private val database: MongoDatabase = mongoClient.getDatabase(config.getString("mongo.database"))
    .withCodecRegistry(codecRegistry)

  def db(): MongoDatabase = database
  def stopRepository() = new StopRepository
  def vehiclesRepository() = new VehiclesRepository
  def passagesRepository() = new PassagesRepository
}
