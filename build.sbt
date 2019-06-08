lazy val common = Seq(
  version := "0.1",
  scalaVersion := "2.12.8",
)

lazy val scraper = project.in(file("scraper"))
  .settings(
    common,
    name := "NeverRideOnTimeScraper",
    retrieveManaged := true,
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.2.0",
      "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
      "com.typesafe.akka" %% "akka-actor" % "2.5.22",
      "com.typesafe.akka" %% "akka-http" % "10.1.8",
      "com.typesafe.akka" %% "akka-stream" % "2.5.22",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    )
  ).
  settings(
    mainClass in assembly := Some("Main"),
  )

lazy val analyzer = project.in(file("analyzer"))
  .settings(
    common,
    name := "NeverRideOnTimeAnalyzer",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.4.3",
      "org.apache.spark" %% "spark-sql" % "2.4.3",
      "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.0",
      "org.scalactic" %% "scalactic" % "3.0.5",
      "org.scalatest" %% "scalatest" % "3.0.5" % "test"
    )
  )

lazy val root = (project in file("."))
  .aggregate(scraper, analyzer)