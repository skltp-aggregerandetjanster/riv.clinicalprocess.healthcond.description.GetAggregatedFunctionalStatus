package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import riv.clinicalprocess.healthcond.description._2.ResultType;
import riv.clinicalprocess.healthcond.description.enums._2.ResultCodeEnum;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusResponseType;
import riv.clinicalprocess.healthcond.description.getfunctionalstatusresponder.v2.GetFunctionalStatusType;
import se.skltp.aggregatingservices.AgServiceFactoryBase;

@Log4j2
public class GAFSAgpServiceFactoryImpl extends
    AgServiceFactoryBase<GetFunctionalStatusType, GetFunctionalStatusResponseType>{

@Override
public String getPatientId(GetFunctionalStatusType queryObject){
    return queryObject.getPatientId().getId();
    }

@Override
public String getSourceSystemHsaId(GetFunctionalStatusType queryObject){
    return queryObject.getSourceSystemHSAId();
    }

@Override
public GetFunctionalStatusResponseType aggregateResponse(List<GetFunctionalStatusResponseType> aggregatedResponseList ){

    GetFunctionalStatusResponseType aggregatedResponse=new GetFunctionalStatusResponseType();

    for (Object object : aggregatedResponseList) {
        GetFunctionalStatusResponseType response = (GetFunctionalStatusResponseType) object;
        aggregatedResponse.getFunctionalStatusAssessment().addAll(response.getFunctionalStatusAssessment());
    }
    
    aggregatedResponse.setResult(new ResultType());
    aggregatedResponse.getResult().setResultCode(ResultCodeEnum.INFO);
    aggregatedResponse.getResult().setLogId("NA");

    return aggregatedResponse;
    }
}

