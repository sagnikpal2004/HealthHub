package nic.HealthHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.json.JSONObject;

// import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;

import nic.HealthHub.repository.ObservationRepository;
// import nic.HealthHub.repository.DeviceRepository;


/**
 * The HL7Service class is designed for creating and managing HL7 FHIR Observation resources within a Spring Boot application. 
 * It leverages the FHIR DSTU3 model to construct Observation instances for clinical observations, 
 * utilizing services like InterpretationService for value interpretation based on SNOMED CT codes. 
 */
@Service
public class MQTTListenerService implements MqttCallback {

    @Autowired private HL7Service fhir;
    @Autowired private ObservationRepository observationRepo;
    // @Autowired private DeviceRepository deviceRepo;

    /**
     * Constructs an MQTTListenerService and sets this service as the callback handler for the provided MqttClient.
     * 
     * @param client The MqttClient to listen to.
     * @throws MqttException if an error occurs setting the callback on the MqttClient.
     */
    public MQTTListenerService(MqttClient client) throws MqttException{
        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable cause) { }

    /**
     * Processes incoming MQTT messages, extracting observation data and saving them as FHIR Observations.
     * 
     * @param topic The MQTT topic on which the message arrived.
     * @param message The actual message.
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        JSONObject data = new JSONObject(message.toString());
        
        String device_id = data.getString("device_id");
        // String patient_id = deviceRepo.findById(new IdType(device_id)).get().getPatient().getReference();
        String patient_id = "patient-0";

        JSONObject observables = data.getJSONObject("observables");
        for (String key : observables.keySet()) {
            int value = observables.getInt(key);

            Observation obs = fhir.createObservation(device_id, patient_id, key, value);
            observationRepo.save(obs);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }    
}
