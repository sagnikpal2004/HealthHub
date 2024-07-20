package nic.HealthHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;


@Configuration
public class HL7Config {
    private static final String HAPI_SERVER = "https://hapi.fhir.org/baseR4";
    
    @Bean
    public IGenericClient fhirClient() {
        return FhirContext.forR4().newRestfulGenericClient(HAPI_SERVER);
    }
}
