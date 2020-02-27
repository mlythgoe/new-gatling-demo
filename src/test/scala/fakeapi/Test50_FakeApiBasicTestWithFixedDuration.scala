package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test50_FakeApiBasicTestWithFixedDuration extends Simulation {

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

  // Run a scenario forever - otherwise the scenario will just run once for a user
  val scenarioBuilder = scenario("Basic Load Simulation")
    .forever() {
      exec(getAllBooks())
        .exec(getSpecificBook())
        .exec(getAllBooks())
    }

  setUp(
    scenarioBuilder.inject(
      nothingFor(5 seconds), // pause for a given duration
      atOnceUsers(50) // inject a number of users
    ).protocols(httpConf)
  ).maxDuration(1 minute) // Defining a duration is good for a soak test.

}

