import com.mongodb.spark.MongoSpark
import org.apache.spark.sql.SparkSession

object Main extends App {
  val spark = initSpark()

  val dataService = new DataService(spark)

  dataService.passages()

  def initSpark() = {
    SparkSession.builder()
      .master("local")
      .appName("NeverRideOnTimeAnalyzer")
      .config("spark.mongodb.input.uri", "mongodb://127.0.0.1:27017/mpk.passages")
      .getOrCreate()
  }
}
