package simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.netty.handler.codec.http.{HttpHeaderNames, HttpHeaderValues}

class BasicSimulation extends  Simulation {

  val httpConf = http.baseUrl("http://localhost:3000")

  /***
   * Perform login of the customer on 'oauth' system
   */
  val getAllPosts = exec(http("Get all posts")
    .get("/posts")
    .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
    .check(status.is(200))
  )

  /**
   * Executing the scenario and all steps of test
   */
  val scn = scenario("Scenario example").exec(
    getAllPosts
  )

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}