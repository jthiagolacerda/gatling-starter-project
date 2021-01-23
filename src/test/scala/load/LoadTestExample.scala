package load

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.netty.handler.codec.http.{HttpHeaderNames, HttpHeaderValues}

class LoadTestExample extends Simulation {

  val httpProtocol = http.baseUrl(
    EnvironmentConfiguration().getBaseUrl())
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

  val scn = scenario("Scenario to create a new post")
    .exec(sendPostRequest)

  val env_ramp_user = (EnvironmentConfiguration().getProperty("ramp_user", "10")).toInt
  val env_duration = (EnvironmentConfiguration().getProperty("duration", "10")).toInt

  setUp(
    scn.inject(constantUsersPerSec(env_ramp_user) during (env_duration)
    ).protocols(httpProtocol)
  )
}