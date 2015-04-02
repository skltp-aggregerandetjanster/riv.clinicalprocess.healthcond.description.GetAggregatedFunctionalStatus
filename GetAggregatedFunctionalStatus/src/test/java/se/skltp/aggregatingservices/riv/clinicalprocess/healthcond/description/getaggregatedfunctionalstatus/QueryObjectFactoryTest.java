package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.w3c.dom.Node;

import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryTest {

    private QueryObjectFactory objectUnderTest = new QueryObjectFactoryImpl();

    // TODO - either implement or delete
    
    @Test
    @Ignore
    public void testQueryObjectFactory() {
        Node node = null;
        objectUnderTest.createQueryObject(node);
        assertEquals("expected", "actual");
    }
}
