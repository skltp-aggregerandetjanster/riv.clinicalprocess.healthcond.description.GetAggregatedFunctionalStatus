package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus

trait CommonParameters {
  val serviceName:String     = "FunctionalStatus"
  val urn:String             = "urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatusResponder:2"
  val responseElement:String = "GetFunctionalStatusResponse"
  val responseItem:String    = "functionalStatusAssessment"
  var baseUrl:String         = if (System.getProperty("baseUrl") != null && !System.getProperty("baseUrl").isEmpty()) {
                                   System.getProperty("baseUrl")
                               } else {
                                   "http://33.33.33.33:8081/GetAggregatedFunctionalStatus/service/v2"
                               }
}
