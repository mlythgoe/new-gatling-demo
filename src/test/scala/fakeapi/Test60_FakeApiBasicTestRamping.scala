package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test60_FakeApiBasicTestRamping extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")

  def getAllBooks() = {
    exec(
      http("Get all books")
        .get("Books")
    )
  }

  def getSpecificBook() = {
    exec(
      http("Get Specific Book")
        .get("Books/2")
    )
  }

  val scenarioBuilder = scenario("Basic Load Simulation")
    .forever() {
      exec(getAllBooks())
        .exec(getSpecificBook())
        .exec(getAllBooks())
    }

  setUp(
    scenarioBuilder.inject(
      nothingFor(5 seconds), //  Pause for 30 seconds
      atOnceUsers(100), // Injects 100 users at once
      rampUsers(10) during (5 seconds), // Inject a given number of users with a linear ramp up
                                               //  over a given duration

      constantUsersPerSec(20) during(120 seconds) // Inject users at a constant rate of
                                                    // users per second, for a given duration
    ).protocols(httpConf)
  ).maxDuration(2 minute)

}

