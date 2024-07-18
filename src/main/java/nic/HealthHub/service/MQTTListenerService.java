package nic.HealthHub.service;

import org.springframework.stereotype.Service;
// import org.springframework.beans.factory.annotation.Autowired;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


@Service
public class MQTTListenerService implements MqttCallback {
    private static final String TOPIC = "observables";

    // @Autowired
    public MQTTListenerService(MqttClient client) throws MqttException{
        client.subscribe(TOPIC);
        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable cause) { }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) { }    
}
