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
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import se.skltp.agp.service.api.QueryObject;

public class QueryObjectFactoryImplTest {

    private QueryObjectFactoryImpl objectUnderTest = new QueryObjectFactoryImpl();
    
    String getFunctionalStatusString =
            
    "<urn1:GetFunctionalStatus                                                                      " + 
    " xmlns:urn1=\"urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatusResponder:2\"  " +
    " xmlns:urn2=\"urn:riv:clinicalprocess:healthcond:description:2\"                               " +
    ">                                                                                              " + 
    "  <urn1:patientId>                                                                             " +
    "    <urn2:id>121212121212</urn2:id>                                                            " +
    "    <urn2:type>1.2.752.129.2.1.3.1</urn2:type>                                                 " +
    "  </urn1:patientId>                                                                            " + 
    "  <urn1:datePeriod>                                                                            " +
    "    <urn2:start>20100401063100</urn2:start>                                                    " +
    "    <urn2:end>20200331235959</urn2:end>                                                        " +
    "  </urn1:datePeriod>                                                                           " + 
    "  <urn1:sourceSystemHSAId>zxcvb</urn1:sourceSystemHSAId>                                       " +
    "  <urn1:careContactId>yhn</urn1:careContactId>                                                 " +
    "</urn1:GetFunctionalStatus>                                                                    ";
    
    @Test
    public void testFindContentCategorization() {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
            Element node = factory
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(getFunctionalStatusString.getBytes()))
                    .getDocumentElement();
            
            QueryObject qo = objectUnderTest.createQueryObject(node);
            assertEquals(null,qo.getFindContent().getCategorization());
        } catch (SAXException e) {
            fail(e.getLocalizedMessage());
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Test
    public void testFindContentEiServiceDomain() {
        
        objectUnderTest.setEiServiceDomain("clinicalprocess:healthcond:description");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
            Element node = factory
                    .newDocumentBuilder()
                    .parse(new ByteArrayInputStream(getFunctionalStatusString.getBytes()))
                    .getDocumentElement();
            
            QueryObject qo = objectUnderTest.createQueryObject(node);
            assertEquals("clinicalprocess:healthcond:description",qo.getFindContent().getServiceDomain());
        } catch (SAXException e) {
            fail(e.getLocalizedMessage());
        } catch (IOException e) {
            fail(e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            fail(e.getLocalizedMessage());
        }
    }
}

