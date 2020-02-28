package fakeapi.loadsimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicLoadSimulation3 extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/videogames")
    .header("Accept", "application/json")

  def getAllBooks() = {
    exec(
      http("Get all Video Games")
        .get("/")
        .check(status.is(200))
    )
  }

  def getSpecificBook() = {
    exec(
      http("Get Specific Book")
        .get("/1")
        .check(status.is(200))
    )
  }

  val scenarioBuilder = scenario("Basic Load Simulation 3")
    .forever() {
      repeat(10) {
        exec(getAllBooks())
          .pause(5)
          .exec(getSpecificBook())
          .pause(5)
          .exec(getAllBooks())
      }
    }

  setUp(

    scenarioBuilder.inject(
      constantConcurrentUsers(50) during (10 seconds),
      rampConcurrentUsers(50) to (200) during (2 minutes),
      rampConcurrentUsers(200) to (500) during (2 minutes),
    ).protocols(httpConf)
  ).maxDuration(10 minute) // Defining a duration is good for a soak test.

}
