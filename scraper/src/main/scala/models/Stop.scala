package models

import org.bson.types.ObjectId


case class Stop(name: String, _id: ObjectId = new ObjectId())