package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test20_FakeApiBasicTestWithPause extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")

  val scenarioBuilder = scenario("Basic Load Simulation")
    .exec(
      http("Get all books")
        .get("Books")
    )
    .pause(5) // pause 5 seconds

    .exec(
      http("Get Specific Book")
        .get("Books/2")
    )
    .pause(1, 20) // pause for a random number of seconds between 1 and 20

    .exec(
      http("Get all books")
        .get("Books")
    )
    .pause(3000.milliseconds) // pause for 3000 milliseconds

  setUp(
    scenarioBuilder.inject(
      nothingFor(5 seconds),
      atOnceUsers(1)
    ).protocols(httpConf)
  )

}

