package main.java.mqttclient.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.mqttclient.model.ClientMessage;
import main.java.mqttclient.model.ClientTopic;
import main.java.mqttclient.model.MessagesModel;
import main.java.mqttclient.observer.ObserverException;
import main.java.mqttclient.observer.Update;

import java.util.stream.Collectors;


/**
 * mqtt-client
 *
 * @author jan
 */
public class MessagesController {

    private MessagesModel messagesModel;
    private ObservableList<ClientTopic> clientMessageData;
    private ObservableList<String> messagesList = FXCollections.observableArrayList();


    @FXML
    private Label topicNameLabel;

    @FXML
    private TableView<ClientTopic> topicsTable;

    @FXML
    private TableColumn<ClientTopic, String> messageColumn;

    @FXML
    ListView<String> messagesListView;

    public MessagesController() {

    }

    @FXML
    private void initialize() {

        clientMessageData = FXCollections.observableArrayList();
        topicsTable.setItems(clientMessageData);

        messageColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        showMessageDetails(null);

        topicsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMessageDetails(newValue));

    }

    private void showMessageDetails(ClientTopic clientTopic) {
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

    public void setModel(MessagesModel messagesModel) {
        this.messagesModel = messagesModel;

        this.clientMessageData.addAll(this.messagesModel.getClientTopicData());
        try {
            this.messagesModel.add(this);
        } catch (ObserverException e) {
            e.printStackTrace();
        }
    }

    @Update
    public void update() {

    }
}
