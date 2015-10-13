package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.mockito.Mockito;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentBodyType;
import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentType;
import riv.clinicalprocess.healthcond.description._2.PatientSummaryHeaderType;
import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryTest {

    private final static String SUBJECT_OF_CARE = UUID.randomUUID().toString();
    private final static int NUMBER_OF_RESPONSES = 5;

    private final ResponseListFactoryImpl objectUnderTest = new ResponseListFactoryImpl();
    private final List<Object> responseList = new ArrayList<Object>();

    private final QueryObject queryObject = Mockito.mock(QueryObject.class);
    private final FindContentType findContentType = Mockito.mock(FindContentType.class);

    @Before
    public void setup() {
        Mockito.when(findContentType.getRegisteredResidentIdentification()).thenReturn(SUBJECT_OF_CARE);
        Mockito.when(queryObject.getFindContent()).thenReturn(findContentType);

        for (int i = 0; i < NUMBER_OF_RESPONSES; i++) {
            final GetFunctionalStatusResponseType resp = new GetFunctionalStatusResponseType();
            final FunctionalStatusAssessmentType record = new FunctionalStatusAssessmentType();
            record.setFunctionalStatusAssessmentBody(new FunctionalStatusAssessmentBodyType());
            record.setFunctionalStatusAssessmentHeader(new PatientSummaryHeaderType());
            record.getFunctionalStatusAssessmentHeader().setPatientId(new PersonIdType());
            record.getFunctionalStatusAssessmentHeader().getPatientId().setId(SUBJECT_OF_CARE);
            resp.getFunctionalStatusAssessment().add(record);
            responseList.add(resp);
        }
    }

    @Test
    public void testGetXmlFromAggregatedResponse() {
        final JaxbUtil jaxbUtil = new JaxbUtil(GetFunctionalStatusResponseType.class);

        final String after = objectUnderTest.getXmlFromAggregatedResponse(queryObject, responseList);
        
        assertTrue(after.contains("logId>NA<"));
        
        final GetFunctionalStatusResponseType type = (GetFunctionalStatusResponseType) jaxbUtil.unmarshal(after);

        assertEquals(NUMBER_OF_RESPONSES, type.getFunctionalStatusAssessment().size());
        for (FunctionalStatusAssessmentType r : type.getFunctionalStatusAssessment()) {
            assertEquals(SUBJECT_OF_CARE, r.getFunctionalStatusAssessmentHeader().getPatientId().getId());
        }
    }
}
