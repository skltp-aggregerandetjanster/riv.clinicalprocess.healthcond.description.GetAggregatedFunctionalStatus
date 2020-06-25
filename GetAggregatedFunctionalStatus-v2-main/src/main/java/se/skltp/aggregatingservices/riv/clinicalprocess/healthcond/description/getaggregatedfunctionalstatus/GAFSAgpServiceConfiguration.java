package se.skltp.aggregatingservices.riv.clinicalprocess.healthcond.description.getaggregatedfunctionalstatus;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import riv.clinicalprocess.healthcond.description.getfunctionalstatus.v2.rivtabp21.GetFunctionalStatusResponderInterface;
import riv.clinicalprocess.healthcond.description.getfunctionalstatus.v2.rivtabp21.GetFunctionalStatusResponderService;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "getaggregatedfunctionalstatus.v2")
public class GAFSAgpServiceConfiguration extends se.skltp.aggregatingservices.configuration.AgpServiceConfiguration {

  public static final String SCHEMA_PATH = "classpath:/schemas/clinicalprocess_healthcond_description_2.1.3/interactions/GetFunctionalStatusInteraction/GetFunctionalStatusInteraction_2.0_RIVTABP21.wsdl";

  public GAFSAgpServiceConfiguration() {

    setServiceName("GetAggregatedFunctionalStatus-v2");
    setTargetNamespace("urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatus:2:rivtabp21");

    // Set inbound defaults
    setInboundServiceURL("http://localhost:9018/GetAggregatedFunctionalStatus/service/v2");
    setInboundServiceWsdl(SCHEMA_PATH);
    setInboundServiceClass(GetFunctionalStatusResponderInterface.class.getName());
    setInboundPortName(GetFunctionalStatusResponderService.GetFunctionalStatusResponderPort.toString());

    // Set outbound defaults
    setOutboundServiceWsdl(SCHEMA_PATH);
    setOutboundServiceClass(GetFunctionalStatusResponderInterface.class.getName());
    setOutboundPortName(GetFunctionalStatusResponderService.GetFunctionalStatusResponderPort.toString());

    // FindContent
    setEiServiceDomain("riv:clinicalprocess:healthcond:description");
    setEiCategorization(null);

    // TAK
    setTakContract("urn:riv:clinicalprocess:healthcond:description:GetFunctionalStatusResponder:2");

    // Set service factory
    setServiceFactoryClass(GAFSAgpServiceFactoryImpl.class.getName());
    }


}
