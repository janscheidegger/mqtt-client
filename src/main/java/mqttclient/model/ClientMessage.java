package main.java.mqttclient.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * mqqt-client
 *
 * @author jan
 */
public class ClientMessage {

    private final StringProperty topicName;
    private final StringProperty message;


    public ClientMessage(String topicName, String message) {
        this.message = new SimpleStringProperty(message);
        this.topicName = new SimpleStringProperty(topicName);
    }

    public String getTopicName() {
        return topicName.get();
    }

    public StringProperty topicNameProperty() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName.set(topicName);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

}
