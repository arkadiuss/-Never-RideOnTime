---
title: (Never) Ride On Time
author:
  - Arkadiusz Kraus
  - Grzegorz Wcisło
lang: en
section-titles: false
theme: "AnnArbor"
colortheme: "dolphin"
---
# Scraper

## Goal

We wanted to create an application that retrieves data about buses in
Cracow. There are around 1400 bus stops in Cracow and we have to query
each one separately for information about the whereabouts of nearby
buses.

## The API

### Servers

MPK has two servers, one for buses and one for trams. We decided to go only for buses.

#### Bus API

[http://91.223.13.70/internetservice](http://91.223.13.70/internetservice)

#### Tram API

[http://ttss.krakow.pl/internetservice](http://ttss.krakow.pl/internetservice)

### Endpoints - Vehicles

#### URL

[geoserviceDispatcher/services/vehicleinfo/vehicles](geoserviceDispatcher/services/vehicleinfo/vehicles)

#### Sample response

```json
{ "lastUpdate": 1560066842348,
  "vehicles": [{
    "color": "0x000000",
    "heading": 90,
    "latitude": 180069486,
    "name": "133 Bieżanów Potrzask",
    "tripId": "8095261304192117256",
    "id": "-1152921495675693247",
    "category": "bus",
    "longitude": 71798625
}] }
```

### Endpoints - Stop Info

#### URL

[geoserviceDispatcher/services/stopinfo/stops](geoserviceDispatcher/services/stopinfo/stops)

#### Sample response

```json
{ "stops": [{
    "category": "bus",
    "id": "8095258838875244269",
    "latitude": 180353736,
    "longitude": 72333180,
    "name": "Grzegorza z Sanoka (nż)",
    "shortName": "3186"
}] }
```

### Endpoints - Stops

#### URL

[services/passageInfo/stopPassages/stop?stop=ID](services/passageInfo/stopPassages/stop?stop=ID)

#### Sample response

```json
{ "actual": [{
    "actualRelativeTime": 1263,
    "direction": "Ruszcza",
    "mixedTime": "10:26",
    "passageid": "-1152921504324670730",
    "patternText": "160",
    "plannedTime": "10:26",
    "routeId": "8095257447305838788",
    "status": "PLANNED",
    "tripId": "8095261304193399306"
}] ... }
```

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

The VM has only 1GB of RAM and the JVM would really like to use
all of it. Adding 512MB of swap space seems to work wonders.

### Random scraper stops

After about 24 hours of working the scrapper suddenly stops
without any error messages. We suspect the lack of RAM might have
something to do with this.  To combat this problem we have a
cronjob that checks whether the scrapper is running and restarts
the container if need be.

## Result

We have created a scraper that has many failover mechanisms, can
handle VM restarts, API downtime and random crashes from unidentified
reasons. Since we added all of the above features, it has been
running flawlessly.

It scrapes data at the maximum rate possible, using as many concurrent
connections as possible.
