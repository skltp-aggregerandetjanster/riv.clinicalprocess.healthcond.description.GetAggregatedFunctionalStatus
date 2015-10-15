package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                    log.debug("AggregatedFunctionalStatus - categorization {}", eic);
                }
            }
        }
        if (eiCategorizations.isEmpty()) {
            throw new RuntimeException("AggregatedFunctionalStatus - no categorizations specified in configuration (EI_CATEGORIZATIONS)");
        }
    }

    /**
     * Engagement index has responded with a list of engagements.
     * Retrieve source system hsa id from the engagement and prepare a list of source systems together with the original request.
     * This list will be split by mule and sent to each source system.
     */
    public List<Object[]> createRequestList(QueryObject qo, FindContentResponseType src) {

        FindContentResponseType findContentResponse = (FindContentResponseType) src;
        List<EngagementType> engagements = findContentResponse.getEngagement();
        log.debug("Got {} hits in the engagement index", engagements.size());
        
        Set<String> sourceSystems = new HashSet<String>(); // set of unique source system hsa ids
        
        // remove unsupported categorizations; remove duplicate sourceSystems
        for (EngagementType engagement : engagements) {
            if (isCategorization(engagement.getCategorization())) {
                // If supported categorization - add to set
                sourceSystems.add(engagement.getSourceSystem());
            }
        }

        log.debug("Preparing to call {} different source systems", sourceSystems.size());
        List<Object[]> requestsToBeSentToSourceSystems = new ArrayList<Object[]>();
        GetFunctionalStatusType request = (GetFunctionalStatusType) qo.getExtraArg(); // consumer's original request

        for (String sourceSystem : sourceSystems) {
            log.info("Preparing to call source system {} for subject of care id {}", sourceSystem, request.getPatientId() == null ? null : request.getPatientId().getId());
            // the original request is sent unchanged to each sourceSystem
            Object[] reqArr = new Object[] { sourceSystem, request };
            requestsToBeSentToSourceSystems.add(reqArr);
        }

        log.debug("Transformed payload: {}", requestsToBeSentToSourceSystems);
        return requestsToBeSentToSourceSystems;
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
}