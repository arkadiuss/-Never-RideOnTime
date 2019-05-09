lazy val common = Seq(
  version := "0.1",
  scalaVersion := "2.12.8",
)

lazy val scraper = project.in(file("scraper"))
    .settings(
      common,
      name := "NeverRideOnTimeScraper",
      resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.2.0",
        "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
        "com.typesafe.akka" %% "akka-actor" % "2.5.22",
        "com.typesafe.akka" %% "akka-http" % "10.1.8",
      )
)

lazy val analyzer = project.in(file("analyzer"))
  .settings(
    common,
    name := "NeverRideOnTimeAnalyzer"
  )

lazy val root = (project in file("."))
  .aggregate(scraper, analyzer)