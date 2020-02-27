package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test10_FakeApiBasicTest extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")

  val scenarioBuilder = scenario("Basic Load Simulation")

    .exec(
      http("Get all books")
        .get("Books")
    )

    .exec(
      http("Get Specific Book")
        .get("Books/2")
    )

    .exec(
      http("Get all books")
        .get("Books")
    )

  setUp(
    scenarioBuilder.inject(
      nothingFor(5 seconds), // pause for a given duration
      atOnceUsers(1) // inject a number of users
    ).protocols(httpConf)
  )

}

