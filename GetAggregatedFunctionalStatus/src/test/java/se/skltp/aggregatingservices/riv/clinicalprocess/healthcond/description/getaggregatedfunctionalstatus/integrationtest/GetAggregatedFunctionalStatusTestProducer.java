package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.integrationtest;

import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description.getfunctionalstatus.v2.rivtabp21.GetFunctionalStatusResponderInterface;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.agp.test.producer.TestProducerDb;

@WebService(serviceName = "GetFunctionalStatusResponderService", portName = "GetFunctionalStatusResponderPort", targetNamespace = "urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatus:2:rivtabp21", name = "GetFunctionalStatusInteraction")
public class GetAggregatedFunctionalStatusTestProducer implements GetFunctionalStatusResponderInterface {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedFunctionalStatusTestProducer.class);

    private TestProducerDb testDb;

    public void setTestDb(TestProducerDb testDb) {
        this.testDb = testDb;
    }

    public GetFunctionalStatusResponseType getFunctionalStatus(String logicalAddress, GetFunctionalStatusType request) {
        
        if (request == null) {
            throw new RuntimeException("GetFunctionalStatusType is null");
        }
        if (request.getPatientId() == null) {
            throw new RuntimeException("GetFunctionalStatusType.getPatientId is null");
        }
        if (StringUtils.isBlank(request.getPatientId().getId())) {
            throw new RuntimeException("GetFunctionalStatusType.patientId.id is blank");
        }
        
        GetFunctionalStatusResponseType response = null;

        log.info("### Virtual service for GetFunctionalStatus call the source system with logical address: {} and patientId: {}", 
                logicalAddress, request.getPatientId().getId());

        response = (GetFunctionalStatusResponseType)testDb.processRequest(logicalAddress, request.getPatientId().getId());
        if (response == null) {
            // Return an empty response object instead of null if nothing is found
            response = new GetFunctionalStatusResponseType();
        }

        log.info("### Virtual service got {} bookings in the reply from the source system with logical address: {} and patientId: {}", 
                new Object[] {response.getFunctionalStatusAssessment().size(), logicalAddress, request.getPatientId().getId()});

        return response;
    }
}