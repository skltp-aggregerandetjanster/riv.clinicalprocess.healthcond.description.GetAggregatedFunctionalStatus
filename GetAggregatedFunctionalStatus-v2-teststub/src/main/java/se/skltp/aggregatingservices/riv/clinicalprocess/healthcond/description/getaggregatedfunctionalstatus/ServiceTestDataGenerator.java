package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import lombok.extern.log4j.Log4j2;
import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentBodyType;
import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentType;
import riv.clinicalprocess.healthcond.description._2.PatientSummaryHeaderType;
import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;

import org.apache.cxf.message.MessageContentsList;
import org.springframework.stereotype.Service;
import se.skltp.aggregatingservices.data.TestDataGenerator;

@Log4j2
@Service
public class ServiceTestDataGenerator extends TestDataGenerator {

  @Override
  public String getPatientId(MessageContentsList messageContentsList) {
    GetFunctionalStatusType request = (GetFunctionalStatusType) messageContentsList.get(1);
    String patientId = request.getPatientId().getId();
    return patientId;
  }

  @Override
  public Object createResponse(Object... responseItems) {
    log.info("Creating a response with {} items", responseItems.length);
    GetFunctionalStatusResponseType response = new GetFunctionalStatusResponseType();
    for (int i = 0; i < responseItems.length; i++) {
      response.getFunctionalStatusAssessment().add((FunctionalStatusAssessmentType) responseItems[i]);
    }

    log.info("response.toString:" + response.toString());

    return response;
  }

  @Override
  public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {
    log.debug("Created ResponseItem for logical-address {}, registeredResidentId {} and businessObjectId {}",
        new Object[]{logicalAddress, registeredResidentId, businessObjectId});

    FunctionalStatusAssessmentType response = new FunctionalStatusAssessmentType();

    PatientSummaryHeaderType psht = new PatientSummaryHeaderType();
    PersonIdType pit = new PersonIdType();
    pit.setId(registeredResidentId);
    pit.setType("1.2.752.129.2.1.3.1");
    psht.setPatientId(pit);
    psht.setSourceSystemHSAId(logicalAddress);
    psht.setDocumentId(businessObjectId);
    response.setFunctionalStatusAssessmentHeader(psht);

    FunctionalStatusAssessmentBodyType fsabt = new FunctionalStatusAssessmentBodyType();
    response.setFunctionalStatusAssessmentBody(fsabt);
    return response;
  }

  public Object createRequest(String patientId, String sourceSystemHSAId) {
    GetFunctionalStatusType requestType = new GetFunctionalStatusType();

    requestType.setSourceSystemHSAId(sourceSystemHSAId);
    PersonIdType personIdType = new PersonIdType();
    personIdType.setType("1.2.752.129.2.1.3.1");
    personIdType.setId(patientId);
    requestType.setPatientId(personIdType);

    return requestType;
  }
}
