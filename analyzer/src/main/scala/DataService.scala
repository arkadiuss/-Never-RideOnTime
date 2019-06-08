import com.mongodb.spark.MongoSpark
import functions.CountDiff
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, collect_list, first, desc,
  udf, mean, floor, round, count, substring_index, avg}
import PassagesFields._


class DataService(private val spark: SparkSession) {
  private val dataframe: DataFrame = MongoSpark.load(spark)

  def goodStops(): String = {
    dataframe.select(STOP_SHORT_NAME)
      .distinct()
      .collect()
      .map(f => f.get(0))
      .mkString(",")
  }

  def normalizeDepartedPassages(): DataFrame = {
    val diffUdf = udf(CountDiff)
    dataframe
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
}
