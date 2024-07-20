package nic.HealthHub.service;

import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Observation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HL7Service {

    @Autowired private IGenericClient client;
    @Autowired private InterpretationService interpretationService;
    
    public Observation createObservation(String device_id, String patient_id, String SCTID, int value) {
        Observation observation = new Observation();
        
        observation.setEffective(new DateTimeType());
        observation.setStatus(Observation.ObservationStatus.FINAL);
        observation.getCode().addCoding()
            .setSystem("http://snomed.info/sct")
            .setCode(SCTID)
            .setDisplay(getParameter(SCTID, "display"));
        observation.setValue(new Quantity()
            .setValue(value)
            .setUnit(getParameter(SCTID, "unit"))
            .setSystem("http://unitsofmeasure.org")
        );
        observation.setDevice(new Reference("Device/" + device_id));
        observation.setSubject(new Reference("Patient/" + patient_id));

        CodeableConcept interpretationConcept = new CodeableConcept();
        for (Coding interpretation : interpretationService.getInterpretations(SCTID, value))
            interpretationConcept.addCoding(interpretation);
        observation.setInterpretation(interpretationConcept);

        return observation;
    }

    public Coding createCoding(String SCTID) {
        Coding coding = new Coding();

        coding.setSystem("http://snomed.info/sct");
        coding.setCode(SCTID);
        coding.setDisplay(getParameter(SCTID, "display"));

        return coding;
    }

    private String getParameter(String SCTID, String param) {
        Parameters parameters = new Parameters();
        parameters.addParameter().setName("system").setValue(new StringType("http://snomed.info/sct"));
        parameters.addParameter().setName("code").setValue(new StringType(SCTID));

        Parameters result = client
            .operation()
            .onType(org.hl7.fhir.dstu3.model.CodeSystem.class)
            .named("lookup")
            .withParameters(parameters)
            .execute();

        return result.getParameter().stream()
            .filter(p -> p.getName().equals(param))
            .map(p -> p.getValue().toString())
            .findFirst()
            .orElse("");
    }
}
