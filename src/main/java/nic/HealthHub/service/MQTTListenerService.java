package nic.HealthHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.json.JSONObject;
import org.hl7.fhir.dstu3.model.Observation;
import nic.HealthHub.repository.ObservationRepository;


@Service
public class MQTTListenerService implements MqttCallback {
    private static final String TOPIC = "observations";

    @Autowired private HL7Service fhir;
    @Autowired private ObservationRepository observationRepo;
    @Autowired private MqttClient client;
    public MQTTListenerService() throws MqttException{
        client.subscribe(TOPIC);
        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable cause) { }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        JSONObject data = new JSONObject(message.getPayload());
        
        String device_id = data.getString("device_id");
        String patient_id = "patient-0"; // TODO: Add logic to get patient_id from device_id

        JSONObject observables = data.getJSONObject("observables");
        for (String key : observables.keySet()) {
            int value = observables.getInt(key);

            Observation obs = fhir.createObservation(patient_id, key, value);
            observationRepo.save(obs);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }    
}
