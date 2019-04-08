lazy val common = Seq(
  version := "0.1",
  scalaVersion := "2.12.8",
)

lazy val scraper = project.in(file("scraper"))
    .settings(
      common,
      name := "NeverRideOnTimeScraper",
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.0",
        "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"
      )
)

lazy val analyzer = project.in(file("analyzer"))
  .settings(
    common,
    name := "NeverRideOnTimeAnalyzer"
  )

lazy val root = (project in file("."))
  .aggregate(scraper, analyzer)