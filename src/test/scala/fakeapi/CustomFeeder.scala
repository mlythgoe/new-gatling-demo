package scala.fakeapi

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class CustomFeeder extends Simulation {

  val httpConf = http.baseUrl("https://fakerestapi.azurewebsites.net/api/")
    .header("Accept", "application/json")
    .proxy(Proxy("localhost", 8888))

  var idNumbers = (1 to 2).iterator

//  val customFeeder = Iterator.continually(Map(
//    "ID" -> idNumbers.next(),
//    "Title" -> ("Game-" + getRandomString(5)),
//    "Description" -> getRandomDate(now, rnd),
//    "PageCount" -> rnd.nextInt(100),
//    "Excerpt" -> ("Category-" + getRandomString(6)),
//    "PublishDate" -> ("Rating-" + getRandomString(4))
//  ))

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
