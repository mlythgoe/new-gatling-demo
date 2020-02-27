package scala.fakeapi.loadsimulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")

  def getAllBooks() = {
    exec(
      http("Get all books")
        .get("Books")
        .check(status.is(200))
    )
  }

  def getSpecificBook() = {
    exec(
      http("Get Specific Book")
        .get("Books/2")
        .check(status.is(200))
    )
  }

  val scenarioBuilder = scenario("Basic Load Simulation 1")
    .repeat(10) {
      exec(getAllBooks())
      .pause(5)
      .exec(getSpecificBook())
      .pause(5)
      .exec(getAllBooks())
    }

  setUp(

    scenarioBuilder.inject(
      nothingFor(5 seconds), // Nothing for 5 seconds
      atOnceUsers(5), // 5 users immediately
      rampUsers(50) during (30 seconds)
    ).protocols(httpConf)
  )

}
