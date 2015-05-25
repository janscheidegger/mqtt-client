package main.java.mqttclient.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * mqqt-client
 *
 * @author jan
 */
public class ClientMessage {


    private final StringProperty message;

    public ClientMessage(String message) {
        this.message = new SimpleStringProperty(message);
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
