package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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

}
