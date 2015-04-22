package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import riv.clinicalprocess.healthcond.description._2.DatePeriodType;
import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.ObjectFactory;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryTest {
    
    private static final JaxbUtil ju = new JaxbUtil(GetFunctionalStatusType.class);

    private QueryObjectFactory objectUnderTest = new QueryObjectFactoryImpl();
    
    private ObjectFactory functionalStatusObjectFactory = new ObjectFactory();
    
    @Test
    public void testFindContentPatientId() {
        
        GetFunctionalStatusType getFunctionalStatus = new GetFunctionalStatusType();
        
        getFunctionalStatus.setDatePeriod(new DatePeriodType());
        getFunctionalStatus.getDatePeriod().setStart("123");
        getFunctionalStatus.getDatePeriod().setEnd("123");
        
        getFunctionalStatus.setPatientId(new PersonIdType());
        getFunctionalStatus.getPatientId().setId("111");
        getFunctionalStatus.getPatientId().setType("ttt");
        
        getFunctionalStatus.setSourceSystemHSAId("HSA-ID-789");
        
        String xmlGetFunctionalStatusType = ju.marshal(functionalStatusObjectFactory.createGetFunctionalStatus(getFunctionalStatus));
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
            Element node = factory
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xmlGetFunctionalStatusType.getBytes()))
                    .getDocumentElement();
            
            QueryObject qo = objectUnderTest.createQueryObject(node);
            assertEquals("111",qo.getFindContent().getRegisteredResidentIdentification());
        } catch (SAXException e) {
            fail(e.getLocalizedMessage());
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            fail(e.getLocalizedMessage());
        }
    }
}
