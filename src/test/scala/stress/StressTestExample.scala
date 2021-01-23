package stress

import java.util.concurrent.atomic.AtomicBoolean

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.netty.handler.codec.http.{HttpHeaderNames, HttpHeaderValues}

class StressTestExample extends Simulation {

  val httpProtocol = http.baseUrl(EnvironmentConfiguration().getBaseUrl())
    .header(HttpHeaderNames.AUTHORIZATION, EnvironmentConfiguration().getToken())
    .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
    .header("x-user_customer_id", "customer_id")

  val sendPostRequest = exec(http("Creating new post")
    .post("/posts")
    .body(RawFileBody("requests/createAPost.json"))
    .check(status.is(201))
    .check(jsonPath("$.author").is("jesus-lacerda"))
    .check(jsonPath("$.id"))
  )

  val continue = new AtomicBoolean(true)
  val scn = scenario("Scenario to create a new post")
    .doIf(session => continue.get){
      exec(sendPostRequest)
        .exec( session => {
          if (session.isFailed) { continue.set(false)}
          session
        }
        )
    }

  val env_usersPerSec = (EnvironmentConfiguration().getProperty("user_per_second", "0")).toInt
  val env_times = (EnvironmentConfiguration().getProperty("times", "0")).toInt
  val env_levelLasting = (EnvironmentConfiguration().getProperty("each_level_lasting", "0")).toInt
  val env_separateByRampLasting = (EnvironmentConfiguration().getProperty("separate_by_ramp_lasting", "0")).toInt
  val env_startingFrom = (EnvironmentConfiguration().getProperty("starting_from", "0")).toInt

  setUp(
    scn.inject(
      incrementUsersPerSec(env_usersPerSec)
        .times(env_times)
        .eachLevelLasting(env_levelLasting)
        .separatedByRampsLasting(env_separateByRampLasting)
        .startingFrom(env_startingFrom)
    ).protocols(httpProtocol)
  )

}