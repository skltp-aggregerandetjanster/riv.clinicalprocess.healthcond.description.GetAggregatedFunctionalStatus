package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus

import se.skltp.agp.testnonfunctional.TP04RobustnessAbstract

/**
 * Test VP:GetAggregatedFunctionalStatus over 12 hours
 */
class TP04Robustness extends TP04RobustnessAbstract with CommonParameters {
  // override skltp-box with qa
  if (baseUrl.startsWith("http://33.33.33.33")) {
      baseUrl = "http://ine-sit-app03.sth.basefarm.net:9018/GetAggregatedFunctionalStatus/service/v2"
  }
  setUp(setUpAbstract(serviceName, urn, responseElement, responseItem, baseUrl))
}
