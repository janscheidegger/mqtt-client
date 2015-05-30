package main.java.mqttclient.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.util.ArrayList;
import java.util.List;

/**
 * mqtt-client
 *
 * @author jan
 */
public class ClientTopic {

    private final StringProperty name;
    private final BooleanProperty formattedTopic;
    List<ClientMessage> messages = new ArrayList<ClientMessage>();
    List<KeyValuePair> keyValuePairs = new ArrayList<>();


    public ClientTopic(String topicName, boolean formattedTopic) {
        this.name  = new SimpleStringProperty(topicName);
        this.formattedTopic = new SimpleBooleanProperty(formattedTopic);
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

    public boolean isFormattedTopic() {
        return formattedTopic.get();
    }

    public BooleanProperty formattedTopicProperty() {
        return formattedTopic;
    }

    public void setFormattedTopic(boolean formattedTopic) {
        this.formattedTopic.set(formattedTopic);
    }

    public List<ClientMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ClientMessage> messages) {
        this.messages = messages;
    }

    public List<KeyValuePair> getKeyValuePairs() {
        return keyValuePairs;
    }

    public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }

    public void addKeyValuePair(String key, int value) {
        for(KeyValuePair kvp : keyValuePairs) {
            if(kvp.getKey().equals(key)) {
                kvp.setValue(kvp.getValue() + value);
                return;
            }
        }
        keyValuePairs.add(new KeyValuePair(key, value));
    }
}
