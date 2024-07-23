package nic.HealthHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;


@Configuration
public class MQTTConfig {
    private static final String MQTT_BROKER = "tcp://103.211.23.35:1883";
    private static final String DEVICE_ID = "HealthHub-0";
    private static final String TOPIC = "observations";
    // private static final String USER = "";
    // private static final String PASS = "";

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        // options.setUserName(USER);
        // options.setPassword(PASS);

        MqttClient client = new MqttClient(MQTT_BROKER, DEVICE_ID);
        client.connect(options);
        client.subscribe(TOPIC);

        return client;
    }
}
