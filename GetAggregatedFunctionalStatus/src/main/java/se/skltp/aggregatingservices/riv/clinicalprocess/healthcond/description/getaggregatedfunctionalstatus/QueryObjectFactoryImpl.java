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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;
import org.w3c.dom.Node;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.QueryObjectFactory;

public class QueryObjectFactoryImpl implements QueryObjectFactory {

    private static final Logger log = LoggerFactory.getLogger(QueryObjectFactoryImpl.class);
    private static final JaxbUtil ju = new JaxbUtil(GetFunctionalStatusType.class);

    private String eiServiceDomain;
    public void setEiServiceDomain(String eiServiceDomain) {
        this.eiServiceDomain = eiServiceDomain;
    }

    public void setEiCategorization(String eiCategorization) {
        log.info("eiCategorization is not used within QueryObjectFactoryImpl for GetAggregatedFunctionalStatus - instead the categories are filtered by RequestListFactoryImpl");
    }
    
    /**
     * Transformerar GetFunctionalStatus request till EI FindContent request enligt:
     * 
     * 1. subjectOfCareId                            --> registeredResidentIdentification 
     * 2. riv:clinicalprocess:healthcond:description --> serviceDomain
     */
    public QueryObject createQueryObject(Node node) {
        GetFunctionalStatusType request = (GetFunctionalStatusType) ju.unmarshal(node);
        log.debug("Transformed payload for pid: {}", request.getPatientId().getId());

        FindContentType fc = new FindContentType();
        fc.setRegisteredResidentIdentification(request.getPatientId().getId());
        fc.setServiceDomain(eiServiceDomain);
        fc.setCategorization(null); // no restriction - filter results in RequestListFactoryImpl
        
        QueryObject qo = new QueryObject(fc, request);
        return qo;
    }
}
