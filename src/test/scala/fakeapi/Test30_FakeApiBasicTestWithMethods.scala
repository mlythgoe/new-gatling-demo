package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class Test30_FakeApiBasicTestWithMethods extends Simulation {

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
    .exec(getAllBooks())
    .exec(getSpecificBook())
    .exec(getAllBooks())

  setUp(
    scenarioBuilder.inject(
      nothingFor(5 seconds),
      atOnceUsers(1)
    ).protocols(httpConf)
  )

}

