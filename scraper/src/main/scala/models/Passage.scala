package models

case class Passage(
                    actualRelativeTime: Long,
                    actualTime: Option[String],
                    plannedTime: String,
                    status: String,
                    patternText: String,
                    routeId: String,
                    tripId: String,
                    passageid: String,
                    stopShortName: String,
                    scrapedTimestamp: Long 
                    )

