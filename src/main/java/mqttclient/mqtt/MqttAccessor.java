package main.java.mqttclient.mqtt;

import main.java.mqttclient.observer.Observable;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * mqqt-client
 *
 * @author jan
 */
public class MqttAccessor extends Observable implements MqttCallback {

    private MqttClient mqttClient;
    private final String brokerUrl;
    private boolean currentlyListening = false;

    private int debugCounter = 0;

    private List<MqttMessage> mqttMessages = new ArrayList<>();

    public MqttAccessor(String brokerUrl) {
        this.brokerUrl = brokerUrl;
        try {
            mqttClient = connect(brokerUrl);
        } catch (MqttException e) {
            e.printStackTrace();
            System.err.println("Error while establishing connection to: " + brokerUrl);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Lost Connection to Broker: " +brokerUrl);
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Topic: " + topic + " received message!");
        mqttMessages.add(mqttMessage);
        debugCounter++;
        if (debugCounter > 10) {
            currentlyListening = false;
        }
        notifyObservers();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public void subscribeTopic(String topicName) {
        try {
            mqttClient.setCallback(this);
            mqttClient.subscribe(topicName);
            System.out.println("subscribed to Topic: " + topicName);
            currentlyListening = true;
        } catch (MqttException e) {
            e.printStackTrace();
            System.err.println("Error while connecting to " + topicName + " on Broker :" + brokerUrl);
        }
    }


    public void disconnect() {
        try {
            mqttClient.disconnect();
            System.out.println("Successfully disconnected");
        } catch (MqttException e) {
            System.err.println("Error while disconnecting from Server!");
            e.printStackTrace();
        }
    }

    private MqttClient connect(String brokerUrl) throws MqttException {
        try {
            MqttClient mqttClient = new MqttClient(brokerUrl, UUID.randomUUID().toString());
            mqttClient.connect();
            System.out.println("Connected to " + brokerUrl);
            return mqttClient;
        } catch (MqttException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isCurrentlyListening() {
        return currentlyListening;
    }

    public List<MqttMessage> getMqttMessages() {
        return mqttMessages;
    }
}
