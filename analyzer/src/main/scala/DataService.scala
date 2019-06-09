import com.mongodb.spark.MongoSpark
import functions.CountDiff
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{avg, col, count, desc, first, floor, mean, round, substring_index, udf}
import models.PassagesFields._
import com.mongodb.spark.config.ReadConfig
import models.StopsFields._


class DataService(private val spark: SparkSession) {
  private val dataframePassages: DataFrame = MongoSpark.load(spark, ReadConfig(Map("uri" -> "mongodb://127.0.0.1:27017/mpk.passages")))
  private val dataframeStops: DataFrame = MongoSpark.load(spark, ReadConfig(Map("uri" -> "mongodb://127.0.0.1:27017/mpk.stops")))

  def goodStops(): String = {
    dataframePassages.select(STOP_SHORT_NAME)
      .distinct()
      .collect()
      .map(f => f.get(0))
      .mkString(",")
  }

  def normalizeDepartedPassages(): DataFrame = {
    val diffUdf = udf(CountDiff)
    dataframePassages
      .filter(col(ACTUAL_RELATIVE_TIME) < 0)
      .groupBy(PASSAGE_ID)
      .agg(
        first(LINE_NO) as LINE_NO,
        first(PLANNED_TIME) as PLANNED_TIME,
        first(STOP_SHORT_NAME) as STOP_SHORT_NAME,
        floor(mean(
          diffUdf(
            col(PLANNED_TIME),
            col(ACTUAL_RELATIVE_TIME),
            col(SCRAPED_TIMESTAMP)))) as SEC_DELAY
      )
      .withColumn(DELAY, round(col(SEC_DELAY)/60))
  }

  def passagesCountByDelay(): DataFrame = {
    normalizeDepartedPassages()
      .groupBy(DELAY)
      .agg(count(PASSAGE_ID) as PASSAGES_COUNT)
      .sort(DELAY)
  }

  def passagesByHour(): DataFrame = {
    normalizeDepartedPassages()
      .groupBy(substring_index(col(PLANNED_TIME), ":", 1) as PLANNED_TIME)
      .agg(count(PASSAGE_ID) as PASSAGES_COUNT)
      .sort(PLANNED_TIME)
  }

  def averageDelayByHour(): DataFrame = {
    normalizeDepartedPassages()
      .groupBy(substring_index(col(PLANNED_TIME), ":", 1) as PLANNED_TIME)
      .agg(avg(DELAY) as AVERAGE_DELAY)
      .sort(PLANNED_TIME)
  }


  def averageDelayByBus(): DataFrame = {
    normalizeDepartedPassages()
      .groupBy(LINE_NO)
      .agg(avg(DELAY) as AVERAGE_DELAY)
      .sort(desc(AVERAGE_DELAY))
  }

  def averageDelayByStop(): DataFrame = {
    normalizeDepartedPassages()
      .groupBy(STOP_SHORT_NAME)
      .agg(avg(DELAY) as AVERAGE_DELAY, count(PASSAGE_ID) as PASSAGES_COUNT)
      .sort(desc(AVERAGE_DELAY))
      .join(dataframeStops, col(STOP_SHORT_NAME) === col(SHORT_NAME))
      .select(col(SHORT_NAME), col(NAME), col(AVERAGE_DELAY), col(PASSAGES_COUNT),
        col(LATITUDE)/3600000 as LATITUDE, col(LONGITUDE)/3600000 as LONGITUDE)
  }
}
