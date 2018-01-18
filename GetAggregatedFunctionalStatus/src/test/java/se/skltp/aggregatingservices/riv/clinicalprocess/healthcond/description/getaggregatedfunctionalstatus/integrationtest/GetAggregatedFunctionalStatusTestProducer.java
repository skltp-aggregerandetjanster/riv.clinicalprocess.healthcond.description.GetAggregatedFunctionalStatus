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