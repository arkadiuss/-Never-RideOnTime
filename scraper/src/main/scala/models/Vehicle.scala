package models

class Vehicle(
               val id: String,
               val heading: String,
               val category: String,
               val longitude: Int,
               val latitude: Int,
               val tripId: String,
               val name: String,
               val isDeleted: Boolean
             )
