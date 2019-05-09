package models

import org.bson.types.ObjectId

case class Stop(category: String, id: String, latitude: Int, longitude: Int, name: String, shortName: String)