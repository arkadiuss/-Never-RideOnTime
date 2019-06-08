package functions

import org.joda.time.DateTime

object CountDiff extends ((String, String, Long) => Long) with Serializable {
  override def apply(planned: String, relativeTimeStr: String, timestamp: Long): Long = {
    val splitIndex = planned.indexOf(":")
    val plannedHour = planned.substring(0,splitIndex).toInt
    val plannedMinutes = planned.substring(splitIndex + 1).toInt
    val relativeTime = relativeTimeStr.toLong
    val secondsOfScrap = (new DateTime(timestamp + relativeTime * 1000)).secondOfDay().get()
    val secondsDepart = plannedHour * 3600 + plannedMinutes * 60
    val dlt = secondsOfScrap - secondsDepart
    // we assume that but won't come earlier than 1 hours before planned time
    // and won't be delayed more than 23 hours...
    if(dlt < -60 * 60) dlt + 24*60*60 else dlt
  }
}
