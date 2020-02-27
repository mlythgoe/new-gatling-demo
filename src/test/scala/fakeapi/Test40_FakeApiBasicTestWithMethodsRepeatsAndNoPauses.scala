package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test40_FakeApiBasicTestWithMethodsRepeatsAndNoPauses extends Simulation {

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

  val scenarioBuilder = scenario("Basic Load Simulation")
    .repeat(50) {
      exec(getAllBooks())
    }
    .repeat(10) {
      exec(getSpecificBook())
    }
    .repeat(50 ) {
      exec(getAllBooks())
    }

  setUp(
    scenarioBuilder.inject(
      atOnceUsers(25)
    ).protocols(httpConf)
  )

}

