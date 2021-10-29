package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;


import se.skltp.aggregatingservices.tests.CreateFindContentTest;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GAFSCreateFindContentTest extends CreateFindContentTest {

  public GAFSCreateFindContentTest() {
    super(new ServiceTestDataGenerator(), new GAFSAgpServiceFactoryImpl(), new GAFSAgpServiceConfiguration());
  }

}
