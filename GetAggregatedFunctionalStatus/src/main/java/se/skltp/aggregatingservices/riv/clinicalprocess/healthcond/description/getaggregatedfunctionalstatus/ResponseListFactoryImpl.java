package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soitoolkit.commons.mule.jaxb.JaxbUtil;

import riv.clinicalprocess.healthcond.description.enums._2.ResultCodeEnum;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.ObjectFactory;
import riv.clinicalprocess.healthcond.description._2.ResultType;
import se.skltp.agp.riv.interoperability.headers.v1.ProcessingStatusType;
import se.skltp.agp.service.api.QueryObject;
import se.skltp.agp.service.api.ResponseListFactory;

public class ResponseListFactoryImpl implements ResponseListFactory {

    private static final Logger log = LoggerFactory.getLogger(ResponseListFactoryImpl.class);
    private static final JaxbUtil jaxbUtil = new JaxbUtil(GetFunctionalStatusResponseType.class, ProcessingStatusType.class);
    private static final ObjectFactory OF = new ObjectFactory();

    @Override
    public String getXmlFromAggregatedResponse(QueryObject queryObject, List<Object> aggregatedResponseList) {
        GetFunctionalStatusResponseType aggregatedResponse = new GetFunctionalStatusResponseType();

        for (Object object : aggregatedResponseList) {
            GetFunctionalStatusResponseType response = (GetFunctionalStatusResponseType) object;
            aggregatedResponse.getFunctionalStatusAssessment().addAll(response.getFunctionalStatusAssessment());
        }
        
        aggregatedResponse.setResult(new ResultType());
        aggregatedResponse.getResult().setResultCode(ResultCodeEnum.INFO);

        log.info("Returning {} aggregated FunctionalStatusAssessment for patient id {}", 
                  aggregatedResponse.getFunctionalStatusAssessment().size(),
                  queryObject.getFindContent().getRegisteredResidentIdentification());

        // Since the class GetFunctionalStatusResponseType doesn't have an
        // @XmlRootElement annotation we need to use the ObjectFactory to add it.
        return jaxbUtil.marshal(OF.createGetFunctionalStatusResponse(aggregatedResponse));
    }
}