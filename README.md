# Patronage_18_19_BE
*Simple REST API for room booking system.*

## Prerequisites

* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Gradle](https://docs.gradle.org/current/userguide/installation.html)

## Build with gradle wrapper

    ./gradlew build


## Run 

    ./gradlew bootRun

## REST API documentation
* Open public docs on [swaggerhub.com](https://app.swaggerhub.com/apis/macieg-b/patronage-18_19/1.2.0)
* Run docs locally: *htttp://[base_url]:[port]/swagger-ui.html* and test API with **swagger-ui**

## Example
### Create boardroom
```
curl -X POST "http://localhost:8080/api/boardrooms" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"available\": true, \"equipment\": { \"phone\": { \"extensionNumber\": 89, \"phoneAvailable\": true, \"phoneInterface\": \"BLUETOOTH\", \"publicNumber\": \"+48 123456789\" }, \"projectorName\": \"Projetctor 1\" }, \"floor\": 1, \"hangingSeats\": 0, \"identifier\": \"0.33\", \"lyingSeats\": 10, \"name\": \"Blue room\", \"normalSeats\": 150}"
```
### Create organization
```
curl -X POST "http://localhost:8080/api/organizations" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"name\": \"Organization\"}"
```
### Create reservation
```
curl -X POST "http://localhost:8080/api/reservations" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"boardroom\":{ \"uuid\": \"6444e792-8bdb-439d-8cd0-16abf0c861e0\", \"name\": \"Blue room\", \"identifier\": \"0.33\", \"floor\": 1, \"available\": true, \"normalSeats\": 150, \"lyingSeats\": 10, \"hangingSeats\": 0, \"equipment\": { \"projectorName\": \"Projetctor 1\", \"phone\": { \"phoneAvailable\": true, \"extensionNumber\": 89, \"publicNumber\": \"+48 123456789\", \"phoneInterface\": \"BLUETOOTH\" } }}, \"endDate\": \"2019-01-29T23:55:55.505Z\", \"organization\": { \"uuid\": \"903bce1c-c79e-40e1-987e-4b460394fa5c\", \"name\": \"Organization\"}, \"startDate\": \"2019-01-29T22:55:55.505Z\"}"
```