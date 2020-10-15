package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.skltp.aggregatingservices.tests.CreateRequestListTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class GAFSCreateRequestListTest extends CreateRequestListTest {

  public GAFSCreateRequestListTest() {
    super(new ServiceTestDataGenerator(), new GAFSAgpServiceFactoryImpl(), new GAFSAgpServiceConfiguration());
  }

}