package main.java.mqttclient.mqtt;

import main.java.mqttclient.model.ClientMessage;
import main.java.mqttclient.model.ClientTopic;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * mqqt-client
 *
 * @author jan
 */
public class MqttAccessor extends Observable implements MqttCallback {

    private static final Logger LOGGER = Logger.getLogger(MqttAccessor.class.getName());

    private MqttAccessor() {
    }

    private Map<String, MqttClient> mqttClientMap = new HashMap<>();

    private static volatile MqttAccessor instance = null;

    public static MqttAccessor getInstance() {
        if (instance == null) {
            synchronized (MqttAccessor.class) {
                if (instance == null) {
                    instance = new MqttAccessor();
                }
            }
        }
        return instance;
    }

    private MqttClient connect(String brokerUrl) throws MqttException {

        try {
            MqttClient mqttClient = new MqttClient(brokerUrl, UUID.randomUUID().toString());
            mqttClient.connect();
            System.out.println("Connected to " + brokerUrl);
            mqttClientMap.put(brokerUrl, mqttClient);
            return mqttClient;
        } catch (MqttException e) {
            e.printStackTrace();
            throw e;
        }
    }


    private boolean currentlyListening = false;

    private int debugCounter = 0;

    private List<MqttMessage> mqttMessages = new ArrayList<>();


    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Lost Connection to Broker: ");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Topic: " + topic + " received message!");
        mqttMessages.add(mqttMessage);
        ClientMessage clientMessage = new ClientMessage(topic, mqttMessage.toString());
        setChanged();

        notifyObservers(clientMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public ClientTopic subscribeTopic(String brokerUrl, String topicName, boolean isFormattedTopic) {
        try {
            MqttClient mqttClient;
            if (mqttClientMap.get(brokerUrl) != null) {
                mqttClient = mqttClientMap.get(brokerUrl);

            } else {
                mqttClient = connect(brokerUrl);
            }
            mqttClient.setCallback(this);
            mqttClient.subscribe(topicName);

            System.out.println("subscribed to Topic: " + topicName);
            currentlyListening = true;

            return new ClientTopic(topicName, isFormattedTopic);
        } catch (MqttException e) {
            e.printStackTrace();
            System.err.println("Error while connecting to " + topicName + " on Broker :" + brokerUrl);
        }
        return null;
    }


    public void disconnect(String brokerUrl) {
        try {
            MqttClient mqttClient = mqttClientMap.get(brokerUrl);
            mqttClient.disconnect();
            System.out.println("Successfully disconnected");
        } catch (MqttException e) {
            System.err.println("Error while disconnecting from Server!");
            e.printStackTrace();
        }
    }

    public void disconnectAll() {
        mqttClientMap.forEach((s, mqttClient) -> {
            try {
                mqttClient.disconnect();
                LOGGER.log(Level.FINE, "Disconnected from: {0}", mqttClient.getServerURI());
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }


    public boolean isCurrentlyListening() {
        return currentlyListening;
    }

    public List<MqttMessage> getMqttMessages() {
        return mqttMessages;
    }
}
