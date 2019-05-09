package api

sealed case class Request(url: String)

class StopRequest(stopID: String) extends Request(s"services/passageInfo/stopPassages/stop?stop=$stopID")
class StopsRequest extends Request(s"geoserviceDispatcher/services/stopinfo/stops?left=-648000000&bottom=-324000000&right=648000000&top=324000000")
