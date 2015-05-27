package main.java.mqttclient.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.java.mqttclient.model.ClientMessage;
import main.java.mqttclient.model.ClientTopic;
import main.java.mqttclient.mqtt.MqttAccessor;

import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;


/**
 * mqtt-client
 *
 * @author jan
 */
public class MessagesController implements Observer {


    private MqttAccessor mqttAccessor;
    private ObservableList<String> messagesList = FXCollections.observableArrayList();
    private ObservableList<ClientTopic> topicsList = FXCollections.observableArrayList();


    @FXML
    private Label topicNameLabel;

    @FXML
    private TextField topicNameTextField;


    @FXML
    ListView<ClientTopic> topicsListView;

    @FXML
    ListView<String> messagesListView;

    public MessagesController() {
        mqttAccessor = MqttAccessor.getInstance();
        mqttAccessor.addObserver(this);
    }

    @FXML
    private void initialize() {
        topicsListView.setItems(topicsList);
        topicsListView.setCellFactory(cell -> new ListCell<ClientTopic>() {
            @Override
            protected void updateItem(ClientTopic item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        topicsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMessagesForTopic(newValue));

    }

    @FXML
    private void subscribeTopic() {
        String topicName = topicNameTextField.getText();
        ClientTopic clientTopic = mqttAccessor.subscribeTopic("tcp://iot.eclipse.org:1883" ,topicName);
        topicsList.add(clientTopic);
    }

    private void showMessagesForTopic(ClientTopic clientTopic) {
        if(clientTopic != null) {
            topicNameLabel.setText(clientTopic.getName());
            if(clientTopic.getMessages() != null) {
                messagesList.addAll(clientTopic.getMessages().stream().map(ClientMessage::getMessage).collect(Collectors.toList()));
                messagesListView.getItems().clear();
                messagesListView.setItems(messagesList);
            }
        } else {
            topicNameLabel.setText("");
            messagesListView.setItems(messagesList);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ClientMessage) {

            System.out.println("got message from observable");
            System.out.printf("Topic was: %s%n", ((ClientMessage) arg).getTopicName());
            System.out.printf("message is: %s%n", ((ClientMessage) arg).getMessage());
            String topicName = ((ClientMessage) arg).getTopicName();
            for(ClientTopic topic : topicsList) {
                if(topic.getName().equals(topicName)) {
                    topic.getMessages().add((ClientMessage)arg);
                    System.out.println("added message to topic");
                    return;
                }
            }
            System.out.println("something went wrong!");

        }
    }
}
