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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentBodyType;
import riv.clinicalprocess.healthcond.description._2.FunctionalStatusAssessmentType;
import riv.clinicalprocess.healthcond.description._2.PatientSummaryHeaderType;
import riv.clinicalprocess.healthcond.description._2.PersonIdType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import se.skltp.agp.test.producer.TestProducerDb;

public class GetAggregatedFunctionalStatusTestProducerDb extends TestProducerDb {

    private static final Logger log = LoggerFactory.getLogger(GetAggregatedFunctionalStatusTestProducerDb.class);

    @Override
    public Object createResponse(Object... responseItems) {
        log.info("Creates a response with {} items", responseItems.length);
        GetFunctionalStatusResponseType response = new GetFunctionalStatusResponseType();
        for (int i = 0; i < responseItems.length; i++) {
            response.getFunctionalStatusAssessment().add((FunctionalStatusAssessmentType) responseItems[i]);
        }
        return response;
    }

    @Override
    public Object createResponseItem(String logicalAddress, String registeredResidentId, String businessObjectId, String time) {

        log.debug("Created one response item for logical-address {}, registeredResidentId {} and businessObjectId {}", 
                    new Object[] {logicalAddress, registeredResidentId, businessObjectId });
         
         FunctionalStatusAssessmentType response = new FunctionalStatusAssessmentType();
         
         PatientSummaryHeaderType psht = new PatientSummaryHeaderType();
         PersonIdType pit = new PersonIdType();
         pit.setId(registeredResidentId);
         pit.setType("1.2.752.129.2.1.3.1");
         psht.setPatientId(pit);
         psht.setSourceSystemHSAId(logicalAddress);
         psht.setDocumentId(businessObjectId);
         response.setFunctionalStatusAssessmentHeader(psht);
         
         FunctionalStatusAssessmentBodyType fsabt = new FunctionalStatusAssessmentBodyType();
         response.setFunctionalStatusAssessmentBody(fsabt);
         return response;
    }
}