package fakeapi.loadsimulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicLoadSimulationWithSpike extends Simulation {

  private val httpConf = http.baseUrl("http://localhost:8080/app/videogames")
    .header("Accept", "application/json")

  private def getAllBooks = {
    exec(
      http("Get all Video Games")
        .get("/")
        .check(status.is(200))
    )
  }

  private def getSpecificBook = {
    exec(
      http("Get Specific Book")
        .get("/1")
        .check(status.is(200))
    )
  }

  private val scenarioBuilder: ScenarioBuilder = scenario("Basic Load Simulation 3")
    .forever() {
      repeat(10) {
        exec(getAllBooks)
          //.pause(5)
          .exec(getSpecificBook)
          //.pause(5)
          .exec(getAllBooks)
      }
    }

  setUp(

    scenarioBuilder.inject(
      constantConcurrentUsers(50) during (5 seconds),
      rampConcurrentUsers(50) to 2000 during (15 seconds),
      constantConcurrentUsers(2000) during (30 seconds),
      rampConcurrentUsers(2000) to 0 during (15 seconds)
    ).protocols(httpConf)
  ).maxDuration(3 minute) // Defining a duration is good for a soak test.

}
