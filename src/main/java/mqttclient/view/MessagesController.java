package main.java.mqttclient.view;

import com.sun.deploy.util.SessionState;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import main.java.mqttclient.model.ClientMessage;
import main.java.mqttclient.model.ClientTopic;
import main.java.mqttclient.model.KeyValuePair;
import main.java.mqttclient.model.KeyValuePairList;
import main.java.mqttclient.mqtt.MqttAccessor;
import main.java.mqttclient.parser.FormattedMessageParser;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * mqtt-client
 *
 * @author jan
 */
public class MessagesController implements Observer {


    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    @FXML
    ListView<String> topicsListView;
    @FXML
    ListView<ClientMessage> messagesListView;
    @FXML
    PieChart chart;

    @FXML
    Tab chartTab;

    private MqttAccessor mqttAccessor;
    private FormattedMessageParser formattedMessageParser;
    private ObservableList<ClientMessage> messagesList = FXCollections.observableArrayList();
    private ObservableMap<String, ClientTopic> topicsMap = FXCollections.observableHashMap();
    @FXML
    private Label topicNameLabel;
    @FXML
    private TextField topicNameTextField;
    @FXML
    private CheckBox formattedTopicCheckbox;


    public MessagesController() {
        formattedMessageParser = new FormattedMessageParser();
        mqttAccessor = MqttAccessor.getInstance();
        mqttAccessor.addObserver(this);
    }

    @FXML
    private void initialize() {

        topicsMap.addListener((MapChangeListener<String, ClientTopic>) change -> {
            topicsListView.getItems().removeAll(change.getKey());
            if (change.wasAdded()) {
                topicsListView.getItems().add(change.getKey());
            }
        });



        chart.setData(pieChartData);
        topicsListView.getItems().setAll(topicsMap.keySet());

        topicsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showMessagesForTopic(topicsMap.get(newValue)));
        messagesListView.setItems(messagesList);
        messagesListView.setCellFactory(cell -> new ListCell<ClientMessage>() {
            protected void updateItem(ClientMessage message, boolean empty) {
                super.updateItem(message, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(message.getMessage());
                }
            }
        });
    }

    @FXML
    private void subscribeTopic() throws ExecutionException, InterruptedException {
        String topicName = topicNameTextField.getText();
        boolean isFormattedTopic = formattedTopicCheckbox.isSelected();

        ClientTopic clientTopic = mqttAccessor.subscribeTopic("tcp://iot.eclipse.org:1883", topicName, isFormattedTopic);

        topicsMap.put(topicName, clientTopic);

    }

    private void showMessagesForTopic(ClientTopic clientTopic) {
        if (clientTopic != null) {
            topicNameLabel.setText(clientTopic.getName());
            messagesList.clear();
            if(clientTopic.isFormattedTopic()) {

                chartTab.setDisable(false);
                messagesList.addAll(clientTopic.getKeyValuePairs().stream().map(kvp -> new ClientMessage(clientTopic.getName(), kvp.getKey() + ": " + kvp.getValue())).collect(Collectors.toList()));
                pieChartData.clear();
                pieChartData.addAll(clientTopic.getKeyValuePairs().stream().map(kvp -> new PieChart.Data(kvp.getKey(), kvp.getValue())).collect(Collectors.toList()));
            } else {
                chartTab.setDisable(true);
                messagesList.addAll(clientTopic.getMessages());

            }
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            if (arg instanceof ClientMessage) {
                System.out.println("got message from observable");
                System.out.printf("Topic was: %s%n", ((ClientMessage) arg).getTopicName());
                System.out.printf("message is: %s%n", ((ClientMessage) arg).getMessage());
                String topicName = ((ClientMessage) arg).getTopicName();

                ClientTopic topic = topicsMap.get(topicName);

                if (!topic.isFormattedTopic()) {
                    topic.getMessages().add((ClientMessage) arg);
                }
                else {
                    KeyValuePairList keyValuePairList = formattedMessageParser.parseString(((ClientMessage) arg).getMessage());
                    for (KeyValuePair kvp : keyValuePairList.getKeyValuePairList()) {
                        topic.addKeyValuePair(kvp.getKey(), kvp.getValue());
                    }
                }
                if(topicsListView.getSelectionModel().getSelectedItem() != null && topicsListView.getSelectionModel().getSelectedItem().equals(topicName)) {
                    showMessagesForTopic(topic);
                }
            }
        });

    }
}
