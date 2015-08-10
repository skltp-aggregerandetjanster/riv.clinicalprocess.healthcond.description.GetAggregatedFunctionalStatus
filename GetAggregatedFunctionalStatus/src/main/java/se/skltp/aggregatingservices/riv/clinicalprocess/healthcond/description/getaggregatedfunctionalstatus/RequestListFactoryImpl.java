package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.agp.riv.itintegration.engagementindex.findcontentresponder.v1.FindContentResponseType;
import se.skltp.agp.riv.itintegration.engagementindex.v1.EngagementType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.RequestListFactory;

public class RequestListFactoryImpl implements RequestListFactory {

    private static final Logger log = LoggerFactory.getLogger(RequestListFactoryImpl.class);

    // never null - but can be empty
    private List<String> eiCategorizations = new ArrayList<String>();
    
    /**
     * FunctionalStatus has more than one categorization.
     * Define a list of supported categorizations.
     * @param eicats - comma separated categorization codes - for example fun-fun,pad-pad
     */
    public void setEiCategorizations(String eicats) {
        if (eicats == null || eicats.trim().length() < 1) {
        } else {
            eiCategorizations = Arrays.asList(eicats.split(","));
            if (eiCategorizations.get(0).trim().length() < 1) {
                eiCategorizations.clear();
            } else {
                for (String eic : eiCategorizations) {
                    log.info("AggregatedFunctionalStatus - categorization {}", eic);
                }
            }
        }
        if (eiCategorizations.isEmpty()) {
            throw new RuntimeException("AggregatedFunctionalStatus - no categorizations specified in configuration (EI_CATEGORIZATIONS)");
        }
    }

    /**
     * Filtrera svarsposter från i EI (ei-engagement) baserat parametrar i GetFunctionalStatus requestet (req). 
     * Följande villkor måste vara sanna för att en svarspost från EI skall tas med i svaret:
     * 
     * 1. req.fromDate <= ei-engagement.mostRecentContent <= req.toDate 
     * 2. req.careUnitId.size == 0 or req.careUnitId.contains(ei-engagement.logicalAddress)
     * 
     * Svarsposter från EI som passerat filtreringen grupperas på fältet sourceSystem 
     * samt postens fält logicalAddress (= PDL-enhet) samlas i listan careUnitId per varje sourceSystem
     * 
     * Ett anrop görs per funnet sourceSystem med följande värden i anropet:
     * 
     * 1. logicalAddress  = sourceSystem (systemadressering) 
     * 2. subjectOfCareId = original-request.subjectOfCareId 
     * 3. careUnitId      = listan av PDL-enheter som returnerats från EI för aktuellt source system) 
     * 4. fromDate        = orginal-request.fromDate 5. toDate = orginal-request.toDate
     */
    public List<Object[]> createRequestList(QueryObject qo, FindContentResponseType src) {

        GetFunctionalStatusType originalRequest = (GetFunctionalStatusType) qo.getExtraArg();

        List<String> reqCareUnitList = null;
        reqCareUnitList = originalRequest.getCareUnitHSAId();
        FindContentResponseType eiResp = (FindContentResponseType) src;
        List<EngagementType> inEngagements = eiResp.getEngagement();
        
        log.info("Got {} hits in the engagement index", inEngagements.size());

        Map<String, List<String>> sourceSystem_pdlUnitList_map = new HashMap<String, List<String>>();

        for (EngagementType inEng : inEngagements) {
            // Filter
            if (isPartOf(reqCareUnitList, inEng.getLogicalAddress()) 
                && 
                isCategorization(inEng.getCategorization())) {
                // Add pdlUnit to source system
                log.debug("Add SS: {} for PDL unit: {}", inEng.getSourceSystem(), inEng.getLogicalAddress());
                addPdlUnitToSourceSystem(sourceSystem_pdlUnitList_map, inEng.getSourceSystem(), inEng.getLogicalAddress());
            }
        }

        // Prepare the result of the transformation as a list of request-payloads.
        // one payload for each unique logical-address (e.g. source system since we are using system addressing),
        // each payload built up as an object-array according to the JAX-WS signature for the method in the service interface
        List<Object[]> reqList = new ArrayList<Object[]>();

        for (Entry<String, List<String>> entry : sourceSystem_pdlUnitList_map.entrySet()) {
            String sourceSystem = entry.getKey();
            log.info("Calling source system using logical address {} for subject of care id {}", sourceSystem, originalRequest.getPatientId() == null ? null : originalRequest.getPatientId().getId());
            GetFunctionalStatusType request = originalRequest;
            Object[] reqArr = new Object[] { sourceSystem, request };
            reqList.add(reqArr);
        }

        log.debug("Transformed payload: {}", reqList);
        return reqList;
    }

    private boolean isCategorization(String categorization) {
        if (eiCategorizations.isEmpty()) {
            return true;
        } else if (eiCategorizations.contains(categorization)) {
            log.debug("Processing supported categorization {}", categorization);
            return true;
        } else {
            log.debug("Rejected unsupported categorization {}", categorization);
            return false;
        }
    }

    protected boolean isPartOf(List<String> careUnitIdList, String careUnit) {
        log.debug("Check presence of {} in {}", careUnit, careUnitIdList);
        if (careUnitIdList == null || careUnitIdList.isEmpty()) {
            return true;
        } else {
            return careUnitIdList.contains(careUnit);
        }
    }

    private void addPdlUnitToSourceSystem(Map<String, List<String>> sourceSystem_pdlUnitList_map, String sourceSystem, String pdlUnitId) {
        List<String> careUnitList = sourceSystem_pdlUnitList_map.get(sourceSystem);
        if (careUnitList == null) {
            careUnitList = new ArrayList<String>();
            sourceSystem_pdlUnitList_map.put(sourceSystem, careUnitList);
        }
        careUnitList.add(pdlUnitId);
    }
}