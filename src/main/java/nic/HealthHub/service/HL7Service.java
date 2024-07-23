package nic.HealthHub.service;

// import ca.uhn.fhir.rest.client.api.IGenericClient;

// import org.hl7.fhir.r4.model.Parameters;
// import org.hl7.fhir.r4.model.StringType;
// import org.hl7.fhir.r4.model.UriType;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.IdType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * The HL7Service class is designed for creating and managing HL7 FHIR Observation resources within a Spring Boot application. 
 * It leverages the FHIR DSTU3 model to construct Observation instances for clinical observations, 
 * utilizing services like InterpretationService for value interpretation based on SNOMED CT codes. 
 */
@Service
public class HL7Service {

    // @Autowired private IGenericClient client;
    @Autowired private InterpretationService interpretationService;
    
    /**
     * Creates an Observation object based on the provided parameters. This method is responsible for
     * assembling an Observation instance that encapsulates various details such as the device, patient,
     * and the observation specifics including the SNOMED CT code, value, and interpretation.
     *
     * @param device_id The unique identifier of the device that made the observation.
     * @param patient_id The unique identifier of the patient associated with the observation.
     * @param SCTID The SNOMED CT code that represents the type of observation made.
     * @param value The numerical value of the observation.
     * @return An Observation object populated with the provided details and additional metadata.
     */
    public Observation createObservation(String device_id, String patient_id, String SCTID, int value) {
        Observation observation = new Observation();
        
        observation.setId(generateId("Observation"));
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

    /**
     * Creates a Coding object based on the provided SNOMED CT code (SCTID). This method encapsulates
     * the creation of a Coding instance, which is a standardized way to represent a health condition,
     * symptom, or diagnosis using the SNOMED CT system.
     *
     * @param SCTID The SNOMED CT code that represents a specific health condition or observation.
     * @return A Coding object populated with the system, code, and display text based on the SCTID.
     */
    public Coding createCoding(String SCTID) {
        Coding coding = new Coding();

        coding.setSystem("http://snomed.info/sct");
        coding.setCode(SCTID);
        coding.setDisplay(getParameter(SCTID, "display"));

        return coding;
    }

    /**
     * Retrieves a specific parameter value from the SNOMED CT code system using the FHIR client. This method
     * constructs a FHIR Parameters object to perform a "lookup" operation on the SNOMED CT code system, aiming
     * to fetch a particular detail (parameter) about the provided SNOMED CT code (SCTID).
     *
     * @param SCTID The SNOMED CT identifier for which information is being requested.
     * @param param The specific parameter name whose value is to be retrieved from the lookup operation.
     * @return The value of the requested parameter as a String. If the parameter is not found, returns an empty string.
     */
    private String getParameter(String SCTID, String param) {
    //     Parameters parameters = new Parameters();
    //     parameters.addParameter().setName("system").setValue(new UriType("http://snomed.info/sct"));
    //     parameters.addParameter().setName("code").setValue(new StringType(SCTID));

    //     Parameters result = client
    //         .operation()
    //         .onType(org.hl7.fhir.r4.model.CodeSystem.class)
    //         .named("lookup")
    //         .withParameters(parameters)
    //         .execute();

    //     return result.getParameter().stream()
    //         .filter(p -> p.getName().equals(param))
    //         .map(p -> p.getValue().toString())
    //         .findFirst()
    //         .orElse("");

        switch (SCTID) {
            case "364075005":
                switch (param) {
                    case "display": return "Heart rate";
                    case "unit": return "beats/min";
                }
            case "103228002":
                switch (param) {
                    case "display": return "Blood oxygen saturation";
                    case "unit": return "%";
                }
            case "76863003":
                switch (param) {
                    case "display": return "Normal heart rate";
                }
            case "3424008":
                switch (param) {
                    case "display": return "Tachycardia";
                }
            case "48867003":
                switch (param) {
                    case "display": return "Bradycardia";
                }
            case "449171008":
                switch (param) {
                    case "display": return "Low oxygen saturation";
                }
            case "448225001":
                switch (param) {
                    case "display": return "Normal oxygen saturation";

                }
        }
        return "Unknown SCTID";
    }

    /**
     * Generates a unique identifier for a FHIR resource of the specified type. This method creates an IdType
     * instance using the resource type and the current system time in milliseconds. The resulting IdType is
     * intended for use in uniquely identifying a FHIR resource instance.
     *
     * @param resourceType The type of the FHIR resource for which the ID is being generated (e.g., "Patient", "Observation").
     * @return An IdType object containing a unique identifier for the resource, constructed from the resource type
     *         and the current system time in milliseconds.
     */
    private IdType generateId(String resourceType) {
        return new IdType(resourceType, String.valueOf(System.currentTimeMillis()));
    }
}
