import com.mongodb.spark.MongoSpark
import functions.CountDiff
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{col, collect_list, first, udf, mean, floor}
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


  def normalize_departed_passages(limit: Int = 1000): DataFrame = {
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
      .withColumn(DELAY, floor(col(SEC_DELAY)/60))

  }



}
