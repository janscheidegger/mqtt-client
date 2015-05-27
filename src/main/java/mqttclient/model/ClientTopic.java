package main.java.mqttclient.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * mqtt-client
 *
 * @author jan
 */
public class ClientTopic {

    private final StringProperty name;
    List<ClientMessage> messages = new ArrayList<ClientMessage>();


    public ClientTopic(String topicName) {
        this.name  = new SimpleStringProperty(topicName);
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

}
