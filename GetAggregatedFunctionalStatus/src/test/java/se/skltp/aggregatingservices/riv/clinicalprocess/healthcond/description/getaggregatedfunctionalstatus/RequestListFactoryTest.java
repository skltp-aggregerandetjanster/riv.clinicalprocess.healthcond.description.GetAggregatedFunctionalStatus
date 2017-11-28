/**
 * Copyright (c) 2014 Inera AB, <http://inera.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;

public class RequestListFactoryTest {

    private RequestListFactoryImpl objectUnderTest = new RequestListFactoryImpl();

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
    public void testNoEngagements() {
        objectUnderTest.setEiCategorizations("abc");
        QueryObject eiQueryObject = new QueryObject(new FindContentType(), new GetFunctionalStatusType());
        assertTrue(eiQueryObject.getExtraArg().getClass() == GetFunctionalStatusType.class);
        FindContentResponseType findContentResponse = new FindContentResponseType();
        List<?> l = objectUnderTest.createRequestList(eiQueryObject, findContentResponse);
        assertEquals(0,l.size());
    }
    
    @Test
    public void testMixedEngagements() {
        objectUnderTest.setEiCategorizations("abc");
        QueryObject eiQueryObject = new QueryObject(new FindContentType(), new GetFunctionalStatusType());
        assertTrue(eiQueryObject.getExtraArg().getClass() == GetFunctionalStatusType.class);
        FindContentResponseType findContentResponse = new FindContentResponseType();
        
        EngagementType e = new EngagementType();
        e.setSourceSystem("AAA");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("BBB");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("AAA");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("AAA");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("AAA");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("BBB");
        e.setCategorization("abc");
        findContentResponse.getEngagement().add(e);
        
        e = new EngagementType();
        e.setSourceSystem("CCC");
        e.setCategorization("invalid");
        findContentResponse.getEngagement().add(e);
        
        List<?> l = objectUnderTest.createRequestList(eiQueryObject, findContentResponse);
        assertEquals(2,l.size());
    }
}
