import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession

object Main extends App {
  val spark = SparkSession.builder()
    .master("local")
    .appName("NeverRideOnTimeAnalyzer")
    .config("spark.mongodb.input.uri", "mongodb://127.0.0.1:27017/mpk.passages")
    .getOrCreate()

  val rdd = MongoSpark.load(spark)
  println(rdd.select("stopShortName").distinct().collect().map(f => f.get(0)).mkString(","))
}
