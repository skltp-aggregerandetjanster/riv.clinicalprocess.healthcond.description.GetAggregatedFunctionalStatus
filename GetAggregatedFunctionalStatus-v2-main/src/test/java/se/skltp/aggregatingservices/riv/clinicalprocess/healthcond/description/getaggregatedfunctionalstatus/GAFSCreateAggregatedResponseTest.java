package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import se.skltp.aggregatingservices.api.AgpServiceFactory;
import se.skltp.aggregatingservices.tests.CreateAggregatedResponseTest;


@RunWith(SpringJUnit4ClassRunner.class)
public class GAFSCreateAggregatedResponseTest extends CreateAggregatedResponseTest {

  private static GAFSAgpServiceConfiguration configuration = new GAFSAgpServiceConfiguration();
  private static AgpServiceFactory<GetFunctionalStatusResponseType> agpServiceFactory = new GAFSAgpServiceFactoryImpl();
  private static ServiceTestDataGenerator testDataGenerator = new ServiceTestDataGenerator();

  public GAFSCreateAggregatedResponseTest() {
      super(testDataGenerator, agpServiceFactory, configuration);
  }

  @Override
  public int getResponseSize(Object response) {
        GetFunctionalStatusResponseType responseType = (GetFunctionalStatusResponseType)response;
    return responseType.getFunctionalStatusAssessment().size();
  }
}