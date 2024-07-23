package nic.HealthHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

import org.hl7.fhir.dstu3.model.Coding;


/**
 * Service class for interpreting clinical observation values based on SNOMED CT codes.
 * This service utilizes predefined interpretation ranges to categorize observation values
 * into specific clinical interpretations.
 */
@Service
public class InterpretationService {

    @Autowired private JSONObject interpretationRanges;
    @Autowired private HL7Service fhir;

    /**
     * Retrieves a list of Coding objects representing the clinical interpretations
     * for a given observation value and SNOMED CT code.
     * 
     * Interpretations are determined based on predefined ranges associated with each
     * SNOMED CT code. Each range corresponds to a specific clinical interpretation.
     * 
     * @param SCTID The SNOMED CT identifier for the observation.
     * @param value The numerical value of the observation.
     * @return An ArrayList of Coding objects, each representing a clinical interpretation.
     */
    ArrayList<Coding> getInterpretations(String SCTID, int value) {
        ArrayList<Coding> interpretations = new ArrayList<Coding>();

        JSONObject ranges = interpretationRanges.getJSONObject(SCTID);
        for (String interpretation : ranges.keySet()) {
            JSONArray range = ranges.getJSONArray(interpretation);

            int max = range.isNull(1) ? Integer.MAX_VALUE : range.getInt(1); 
            int min = range.isNull(0) ? Integer.MIN_VALUE : range.getInt(0); 
            if (value >= min && value <= max)
                interpretations.add(fhir.createCoding(interpretation));
        }

        return interpretations;
    }
}
