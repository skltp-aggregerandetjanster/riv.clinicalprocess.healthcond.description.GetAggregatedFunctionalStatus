package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import se.skltp.aggregatingservices.tests.CreateAggregatedResponseTest;


@RunWith(SpringJUnit4ClassRunner.class)
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