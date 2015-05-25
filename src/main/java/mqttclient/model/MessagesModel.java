package main.java.mqttclient.model;

import main.java.mqttclient.observer.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * mqtt-client
 *
 * @author jan
 */
public class MessagesModel extends Observable {
    private List<ClientTopic> clientTopics = new ArrayList<>();

    public MessagesModel() {
        this.clientTopics.add(new ClientTopic("janistcool"));
        this.clientTopics.add(new ClientTopic("Jan"));
    }

    public List<ClientTopic> getClientTopicData() {
        return clientTopics;
    }

    // Todo: Remove Logic Here!
    public void addClientTopic(ClientTopic newClientTopic) {
        clientTopics.add(newClientTopic);
        notifyObservers();
    }
}
