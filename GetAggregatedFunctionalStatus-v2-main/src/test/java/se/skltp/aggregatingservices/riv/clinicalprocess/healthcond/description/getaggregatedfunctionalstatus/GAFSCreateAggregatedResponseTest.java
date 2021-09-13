package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import se.skltp.aggregatingservices.tests.CreateAggregatedResponseTest;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GAFSCreateAggregatedResponseTest extends CreateAggregatedResponseTest {

  public GAFSCreateAggregatedResponseTest() {
    super(new ServiceTestDataGenerator(), new GAFSAgpServiceFactoryImpl(), new GAFSAgpServiceConfiguration());
  }

  @Override
  public int getResponseSize(Object response) {
    GetFunctionalStatusResponseType responseType = (GetFunctionalStatusResponseType) response;
    return responseType.getFunctionalStatusAssessment().size();
  }
}