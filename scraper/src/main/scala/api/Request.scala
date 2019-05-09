package api

sealed case class Request(url: String)

class StopRequest(stopID: String) extends Request(s"services/passageInfo/stopPassages/stop?stop=$stopID")
