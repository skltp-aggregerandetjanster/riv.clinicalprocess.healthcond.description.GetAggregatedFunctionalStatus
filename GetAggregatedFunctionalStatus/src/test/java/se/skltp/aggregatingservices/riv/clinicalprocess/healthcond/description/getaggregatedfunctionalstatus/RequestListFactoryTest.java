package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;

public class RequestListFactoryTest {

    private RequestListFactoryImpl objectUnderTest = new RequestListFactoryImpl();

    @Test
    public void isPartOf() {
        List<String> careUnitIdList = Arrays.asList("UNIT1", "UNIT2");
        assertTrue(objectUnderTest.isPartOf(careUnitIdList, "UNIT2"));
        assertTrue(objectUnderTest.isPartOf(careUnitIdList, "UNIT1"));

        careUnitIdList = new ArrayList<String>();
        assertTrue(objectUnderTest.isPartOf(careUnitIdList, "UNIT1"));

        careUnitIdList = null;
        assertTrue(objectUnderTest.isPartOf(careUnitIdList, "UNIT1"));
    }

    @Test
    public void isNotPartOf() {
        List<String> careUnitIdList = Arrays.asList("UNIT1", "UNIT2");
        assertFalse(objectUnderTest.isPartOf(careUnitIdList, "UNIT3"));
        assertFalse(objectUnderTest.isPartOf(careUnitIdList, null));
    }
    
    @Test
    public void testMostRecentContentNull() {
        objectUnderTest.setEiCategorizations("abc");
        QueryObject qo = new QueryObject(new FindContentType(), new GetFunctionalStatusType());
        assertTrue(qo.getExtraArg().getClass() == GetFunctionalStatusType.class);
        ((GetFunctionalStatusType)qo.getExtraArg()).getCareUnitHSAId().add("HSA-ID-789");
        FindContentResponseType src = new FindContentResponseType();
        src.getEngagement().add(new EngagementType());
        src.getEngagement().get(0).setCategorization("abc");
        src.getEngagement().get(0).setLogicalAddress("HSA-ID-789");
        src.getEngagement().get(0).setMostRecentContent(null);
        List<?> l = objectUnderTest.createRequestList(qo, src);
        assertTrue(l.size() == 1);
    }
    
    @Test
    public void testMostRecentContentEmpty() {
        objectUnderTest.setEiCategorizations("abc");
        QueryObject qo = new QueryObject(new FindContentType(), new GetFunctionalStatusType());
        assertTrue(qo.getExtraArg().getClass() == GetFunctionalStatusType.class);
        ((GetFunctionalStatusType)qo.getExtraArg()).getCareUnitHSAId().add("HSA-ID-789");
        FindContentResponseType src = new FindContentResponseType();
        src.getEngagement().add(new EngagementType());
        src.getEngagement().get(0).setCategorization("abc");
        src.getEngagement().get(0).setLogicalAddress("HSA-ID-789");
        src.getEngagement().get(0).setMostRecentContent("");
        List<?> l = objectUnderTest.createRequestList(qo, src);
        assertTrue(l.size() == 1);
    }
    
    @Test
    public void testMostRecentContentNotTimestamp() {
        objectUnderTest.setEiCategorizations("abc");
        QueryObject qo = new QueryObject(new FindContentType(), new GetFunctionalStatusType());
        assertTrue(qo.getExtraArg().getClass() == GetFunctionalStatusType.class);
        ((GetFunctionalStatusType)qo.getExtraArg()).getCareUnitHSAId().add("HSA-ID-789");
        FindContentResponseType src = new FindContentResponseType();
        src.getEngagement().add(new EngagementType());
        src.getEngagement().get(0).setCategorization("abc");
        src.getEngagement().get(0).setLogicalAddress("HSA-ID-789");
        src.getEngagement().get(0).setMostRecentContent("980511");
        try {
            objectUnderTest.createRequestList(qo, src);
            fail("Exception expected");
        } catch (RuntimeException r) {
            assertTrue(r.getCause().getClass() == ParseException.class);
        }
    }
    
    @Test
    public void testNullTimestamp() {
        boolean b = objectUnderTest.mostRecentContentIsBetween(new Date(), new Date(), null);
        assertTrue(b);
    }
    @Test
    public void testNullFromDate() {
        boolean b = objectUnderTest.mostRecentContentIsBetween(null, new Date(), "19540131153612");
        assertTrue(b);
    }
    @Test
    public void testNullToDate() {
        boolean b = objectUnderTest.mostRecentContentIsBetween(new Date(), null, "19540131153612");
        assertFalse(b);
    }
    @Test
    public void testNullDatesAndTimestamp() {
        boolean b = objectUnderTest.mostRecentContentIsBetween(new Date(), null, "19540131153612");
        assertFalse(b);
    }
    
    @Test
    public void testParseTimestampInvalid() {
        try {
            objectUnderTest.parseOriginalRequestTimeStamp("123");
            fail("Exception expected");
        } catch (RuntimeException r) {
            assertTrue(r.getCause().getClass() == ParseException.class);
        }
    }
    
    @Test
    public void testParseTimestampNull() {
        assertNull(objectUnderTest.parseOriginalRequestTimeStamp(null));
    }

    @Test
    public void testParseTimestamp() {
        Date d = objectUnderTest.parseOriginalRequestTimeStamp("19700131153612");
        assertNotNull(d);
        DateTime expected = new DateTime(1970,01,31,15,36,12,0);
        DateTime actual   = new DateTime(d);
        assertTrue("expecting " + expected + ", but found " + actual, expected.equals(actual));
    }
}
