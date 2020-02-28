package scala.fakeapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._
class CustomFeeder extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")
   // .proxy(Proxy("localhost", 8888)) // Add this line in when you want to capture traffic via Fiddler

  val customFeeder = csv("data/BookCsvFile.csv").circular

  def postNewBook() = {
    repeat(5) {
      feed(customFeeder)
        .exec(http("Post New Game")
          .post("books/")
          .body(ElFileBody("bodies/NewBookTemplate.json")).asJson
          .check(status.is(200)))
        .pause(1)
    }
  }

  val scn = scenario("Post new books")
    .exec(postNewBook())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)

}
