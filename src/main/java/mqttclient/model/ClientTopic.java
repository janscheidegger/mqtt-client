package main.java.mqttclient.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.mqttclient.mqtt.MqttAccessor;
import main.java.mqttclient.observer.ObserverException;
import main.java.mqttclient.observer.Update;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * mqtt-client
 *
 * @author jan
 */
public class ClientTopic {

    private MqttAccessor mqttAccessor;
    private final StringProperty name;
    List<ClientMessage> messages = new ArrayList<ClientMessage>();


    public ClientTopic(String topicName) {
        this.name  = new SimpleStringProperty(topicName);
        mqttAccessor = new MqttAccessor("tcp://iot.eclipse.org:1883");
        mqttAccessor.subscribeTopic(topicName);
        try {
            mqttAccessor.add(this);
        } catch (ObserverException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public List<ClientMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ClientMessage> messages) {
        this.messages = messages;
    }

    public MqttAccessor getMqttAccessor() {
       return mqttAccessor;
    }

    @Update
    public void update() {
        messages.clear();
        for(MqttMessage message : mqttAccessor.getMqttMessages()) {
            messages.add(new ClientMessage(message.getPayload().toString()));
            System.out.println("added Message to Client Messages list");
        }
    }
}
