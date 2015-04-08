package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.scenarios.GetAggregatedFunctionalStatusScenario

/**
 * Test VP:GetAggregatedFunctionalStatus over 12 hours
 */
class TP04Robustness extends Simulation {

  val baseURL = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) { 
                  System.getProperty("baseUrl") 
                } else {
                  "http://ine-sit-app03.sth.basefarm.net:9018/GetAggregatedFunctionalStatus/service/v2"
                }

  val testDuration            =  12 hours
  val numberOfConcurrentUsers =   5
  val rampDuration            =   1 minute
  val minWaitDuration         =   2 seconds
  val maxWaitDuration         =   4 seconds

  val httpProtocol = http.baseURL(baseURL).disableResponseChunksDiscarding

  val robustness = scenario("robustness")
                  .during(testDuration) {
                    feed(csv("patients.csv").circular)
                   .exec(GetAggregatedFunctionalStatusScenario.request)
                   .pause(minWaitDuration, maxWaitDuration)
                  }

  setUp (robustness.inject(rampUsers(numberOfConcurrentUsers) over (rampDuration)).protocols(httpProtocol))
}
