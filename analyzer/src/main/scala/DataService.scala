import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{collect_list, first, udf,col}


class DataService(private val spark: SparkSession) {
  private val dataframe: DataFrame = MongoSpark.load(spark)

  def goodStops(): String = {
    dataframe.select(PassagesFields.shortStopName)
      .distinct()
      .collect()
      .map(f => f.get(0))
      .mkString(",")
  }

  def passages() = {
    val diff = (planned: String, relativeTimeStr: String, timestamp: Long) => {
      val minutes = planned.substring(3).toInt
      val relativeTime = relativeTimeStr.toLong
      if(relativeTime > 0L)  -1
      else {
        val seconds = (timestamp / 1000 + relativeTime) % 3600
        val secondsDepart = minutes * 60
        val dlt = seconds - secondsDepart
        dlt
      }
    }
//    print(diff("15:17", "-28", 1557926308252L))
    val diffUdf = udf(diff)
    dataframe.limit(1000)
      .groupBy(PassagesFields.passageId)
      .agg(
        first(PassagesFields.plannedTime),
        first(PassagesFields.line),
        collect_list(PassagesFields.actualRelativeTime),
        collect_list(PassagesFields.scrapedTimestamp),
        collect_list(
          diffUdf(
            col(PassagesFields.plannedTime),
            col(PassagesFields.actualRelativeTime),
            col(PassagesFields.scrapedTimestamp)))
      )
      .show(20)

  }


}
