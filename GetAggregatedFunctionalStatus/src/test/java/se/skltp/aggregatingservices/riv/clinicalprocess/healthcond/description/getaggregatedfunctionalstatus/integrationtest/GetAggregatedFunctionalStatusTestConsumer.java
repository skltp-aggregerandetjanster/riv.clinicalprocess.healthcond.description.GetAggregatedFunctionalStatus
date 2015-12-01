package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.integrationtest;

import static se.skltp.agp.test.producer.TestProducerDb.TEST_RR_ID_ONE_HIT;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatus.v2.rivtabp21.GetFunctionalStatusResponderInterface;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus.GetAggregatedFunctionalStatusMuleServer;
import se.skltp.agp.test.consumer.AbstractTestConsumer;
import se.skltp.agp.test.consumer.SoapHeaderCxfInterceptor;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;

public class GetAggregatedFunctionalStatusTestConsumer extends AbstractTestConsumer<GetFunctionalStatusResponderInterface> {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedFunctionalStatusTestConsumer.class);

    public static void main(String[] args) {
        String serviceAddress = GetAggregatedFunctionalStatusMuleServer.getAddress("SERVICE_INBOUND_URL");
        String personnummer = TEST_RR_ID_ONE_HIT;

        GetAggregatedFunctionalStatusTestConsumer consumer 
          = new GetAggregatedFunctionalStatusTestConsumer(serviceAddress, SAMPLE_SENDER_ID, SAMPLE_ORIGINAL_CONSUMER_HSAID, SAMPLE_CORRELATION_ID);
        Holder<GetFunctionalStatusResponseType> responseHolder = new Holder<GetFunctionalStatusResponseType>();
        Holder<ProcessingStatusType> processingStatusHolder = new Holder<ProcessingStatusType>();

        consumer.callService("logical-address", personnummer, processingStatusHolder, responseHolder);

        log.info("Returned #timeslots = " + responseHolder.value.getFunctionalStatusAssessment().size());
    }

    public GetAggregatedFunctionalStatusTestConsumer(String serviceAddress, String senderId, String originalConsumerHsaId, String correlationId) {
        // Setup a web service proxy for communication using HTTPS with mutual authentication
        super(GetFunctionalStatusResponderInterface.class, serviceAddress, senderId, originalConsumerHsaId, correlationId);
    }

    public void callService(String logicalAddress, String registeredResidentId, Holder<ProcessingStatusType> processingStatusHolder,
            Holder<GetFunctionalStatusResponseType> responseHolder) {

        log.debug("Calling GetFunctionalStatus-soap-service with registered resident id = {}", registeredResidentId);
        
        GetFunctionalStatusType request = new GetFunctionalStatusType();
        
        PersonIdType personIdType = new PersonIdType();
        personIdType.setType("1.2.752.129.2.1.3.1");
        personIdType.setId(registeredResidentId);
        request.setPatientId(personIdType);

        GetFunctionalStatusResponseType response = _service.getFunctionalStatus(logicalAddress, request);
        responseHolder.value = response;

        processingStatusHolder.value = SoapHeaderCxfInterceptor.getLastFoundProcessingStatus();
    }
}