# Scraper

## Goal

We wanted to create an application that retrieves data about buses in
Cracow. There are around 1400 bus stops in Cracow and we have to query
each one separately for information about the whereabouts of nearby
buses.

## Libraries

### Akka

To download data from MPK servers we use Akka and Akka-Http. We
decided to use Akka mostly because of Akka-Http and for educational
purposes.

### Mongo

We use MongoDB to persist the scraped data.

### Scala-Logging

This library works great with Akka, logs store the information
about actors that create the logs, making it easy to find problems.

## Actors

### Scheduler

Periodically schedules new requests to ApiClient.

### ApiClient

Makes requests using a shared connection pool.

### ApiResponseHandler

Parses responses and passes them to database writers.

### DatabaseWriter

Sends data to MongoDB.

### Logger

Logs stuff.

## Deployment

### Azure

We have a VM on Azure that runs the scraper and MongoDB inside a
docker container.

### Docker

Using sbt we produce a jar file containing the scraper and its
dependencies. The jar is then executed inside a docker container.

## Troubles we had

Turns out making requests to over 1400 endpoints is not that easy.

### Akka-http

Akka has a cap on how many open connections there are to a single
host, and we were breaking that quite regularly. To battle that
we used a connection pool that allows a maximum of 32 open
requests and queues the rest (overflows are dropped, they will be
scheduled again in about a minute).

### Too many stops

To reduce the number of requests we found the most useful stops
(analyzing the data we had) and reduced the number of monitored
ones to about 400.

### Not enough RAM

The VM has only 512MB of RAM and the JVM would really like to use
all of it. Adding 512MB of swap space seems to work wonders.

### Random scraper stops

After about 24 hours of working the scrapper suddenly stops
without any error messages. We suspect the lack of RAM might have
something to do with this.  To combat this problem we have a
cronjob that checks whether the scrapper is running and restarts
the container if need be.
