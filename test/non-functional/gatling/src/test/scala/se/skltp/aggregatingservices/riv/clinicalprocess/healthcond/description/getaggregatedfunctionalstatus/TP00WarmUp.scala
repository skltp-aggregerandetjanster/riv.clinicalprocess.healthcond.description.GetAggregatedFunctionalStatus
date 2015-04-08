package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.scenarios.GetAggregatedFunctionalStatusScenario

/**
 * Simple requests to warm up service.
 */
class TP00WarmUp extends Simulation {

  val baseURL          = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) System.getProperty("baseUrl") else "http://33.33.33.33:8084/GetAggregatedFunctionalStatus/service/v2"

  val testDuration     = 1 minute
  val minWaitDuration  = 2 seconds
  val maxWaitDuration  = 5 seconds
  val times:Int        = 1 // 6

  val httpProtocol = http.baseURL(baseURL).disableResponseChunksDiscarding

  val warmUp = scenario("warm up")
                 .repeat(times) {
                // ---
                // either run all the patients
                // feed(csv("patients.csv").queue)
                // ---
                // or just tolvan tolvansson
                   exec(session => {
                     session.set("status","200").set("patientid","121212121212").set("name","Tolvan Tolvansson").set("count","3")
                   })
                // ---
                   .exec(GetAggregatedFunctionalStatusScenario.request)
                   .pause(1 second)
                  }

  setUp (warmUp.inject(atOnceUsers(1)).protocols(httpProtocol))
}
