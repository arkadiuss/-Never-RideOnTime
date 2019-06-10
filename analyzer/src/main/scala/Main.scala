import org.apache.spark.sql.{DataFrame, SparkSession}

object Main extends App {
  val usage =
    """
      analyzer [command]
      commands: normalized countByDelay passagesByHour avgDelayByHour avgDelayByBus avgDelayByStop
    """
  val DATA_DIR = "data/"

  if(args.length < 1) {
    println(usage)
    sys.exit()
  }

  val spark = initSpark()
  val dataService = new DataService(spark)

  dataService.averageDelayByBus().show(20)
  //executeCommands(args)

  private def initSpark() = {
    SparkSession.builder()
      .master("local")
      .appName("NeverRideOnTimeAnalyzer")
      .getOrCreate()
  }

  private def executeCommands(commands: Array[String]): Unit = {
    for(c <- commands) {
      val (dataframe, filename) = c match {
        case "normalized" => (dataService.normalizeDepartedPassages(), "normalized_passages")
        case "countByDelay" => (dataService.passagesCountByDelay(), "passages_count_by_delay")
        case "passagesByHour" =>  (dataService.passagesByHour(), "passages_by_hour")
        case "avgDelayByHour" =>  (dataService.averageDelayByHour(), "avg_delay_by_hour")
        case "avgDelayByBus" => (dataService.averageDelayByBus(), "avg_delay_by_bus")
        case "avgDelayByStop" => (dataService.averageDelayByStop(), "avg_delay_by_stop")
        case _ => println("Unknown command"); sys.exit(1)
      }
      asCsv(dataframe, filename)

    }
  }

  private def asCsv(dataframe: DataFrame, filename: String): Unit = {
    dataframe
      .coalesce(1)
      .write
      .option("header", "true")
      .csv(s"$DATA_DIR$filename")
  }


}
