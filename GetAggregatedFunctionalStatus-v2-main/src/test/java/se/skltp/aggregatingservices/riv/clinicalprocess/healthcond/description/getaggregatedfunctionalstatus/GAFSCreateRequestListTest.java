package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import se.skltp.aggregatingservices.tests.CreateRequestListTest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GAFSCreateRequestListTest extends CreateRequestListTest {

  public GAFSCreateRequestListTest() {
    super(new ServiceTestDataGenerator(), new GAFSAgpServiceFactoryImpl(), new GAFSAgpServiceConfiguration());
  }

}