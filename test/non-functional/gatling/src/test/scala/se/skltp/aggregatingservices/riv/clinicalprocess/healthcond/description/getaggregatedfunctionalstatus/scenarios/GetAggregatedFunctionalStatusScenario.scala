package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import scala.util.Random

object GetAggregatedFunctionalStatusScenario {

  val headers = Map(
    "Accept-Encoding"                        -> "gzip,deflate",
    "Content-Type"                           -> "text/xml;charset=UTF-8",
    "SOAPAction"                             -> "urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatusResponder:2:GetFunctionalStatus",
    "x-vp-sender-id"                         -> "test",
    "x-rivta-original-serviceconsumer-hsaid" -> "test",
    "Keep-Alive"                             -> "115")

  val request = exec(
        http("GetAggregatedFunctionalStatus ${patientid} - ${name}")
          .post("")
          .headers(headers)
          .body(ELFileBody("GetFunctionalStatus.xml"))
          .check(status.is(session => session("status").as[String].toInt))
          .check(xpath("soap:Envelope", List("soap" -> "http://schemas.xmlsoap.org/soap/envelope/")).exists)
          .check(substring("GetFunctionalStatusResponse"))
          .check(xpath("//ns2:functionalStatusAssessment", List("ns2" -> "urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatusResponder:2")).count.is(session => session("count").as[String].toInt))
      )
}
