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
import main.java.mqttclient.i18n.I18n;
import main.java.mqttclient.model.*;
import main.java.mqttclient.mqtt.MqttAccessor;
import main.java.mqttclient.parser.FormattedMessageParser;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Collections;
import java.util.Locale;
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
    TableView<LogEntry> logEntryTableView;

    @FXML
    TableColumn<LogEntry, String> severityColumn;

    @FXML
    TableColumn<LogEntry, String> timeColumn;

    @FXML
    TableColumn<LogEntry, String> messageColumn;
    @FXML
    PieChart chart;

    @FXML
    Tab chartTab;

    private MqttAccessor mqttAccessor;
    private FormattedMessageParser formattedMessageParser;
    private ObservableList<ClientMessage> messagesList = FXCollections.observableArrayList();
    private ObservableMap<String, ClientTopic> topicsMap = FXCollections.observableHashMap();
    private ObservableList<LogEntry> logData = FXCollections.observableArrayList();

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
        addLogEntry(LogEntry.LogLevel.DEBUG, "Created MqttAccessor Singleton");
    }

    @FXML
    private void initialize() {

        addLogEntry(LogEntry.LogLevel.DEBUG, "Controller Initialized");

        // Listener for changes on Map to update the List
        topicsMap.addListener((MapChangeListener<String, ClientTopic>) change -> {
            topicsListView.getItems().removeAll(change.getKey());
            if (change.wasAdded()) {
                topicsListView.getItems().add(change.getKey());
            }
        });


        // Set Lists to FXML Elements to connect both!
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

        logEntryTableView.setItems(logData);
        messageColumn.setCellValueFactory(cellData -> cellData.getValue().messageProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty().asString());
        severityColumn.setCellValueFactory(cellData -> cellData.getValue().logLevelProperty().asString());



    }

    private void addLogEntry(LogEntry.LogLevel logLevel, String message) {
        logData.add(new LogEntry(logLevel, message));
    }
    @FXML
    private void subscribeTopic() throws ExecutionException, InterruptedException {
        ObservableList<String> styleClass = topicNameTextField.getStyleClass();

        // Check if a valid topic is available Throw error if not!
        if (topicNameTextField.getText().trim().length()==0) {
            addLogEntry(LogEntry.LogLevel.ERROR, "Invalid Topic!");
            if (! styleClass.contains("error")) {
                styleClass.add("error");
            }
            I18n.setLocale(Locale.GERMAN);
            Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getString("error.topicname.missing"));
            alert.show();
        } else {
            // remove all occurrences of error on field!
            styleClass.removeAll(Collections.singleton("error"));
            String topicName = topicNameTextField.getText();
            boolean isFormattedTopic = formattedTopicCheckbox.isSelected();

            // Subscribe to topic on public Broker!
            Platform.runLater(() -> {
                ClientTopic clientTopic = null;
                String brokerUrl = "tcp://iot.eclipse.org:1883";

                try {
                    clientTopic = mqttAccessor.subscribeTopic(brokerUrl, topicName, isFormattedTopic);
                    topicsMap.put(topicName, clientTopic);
                    addLogEntry(LogEntry.LogLevel.DEBUG, String.format("Subscribed to topic %s", clientTopic.getName()));
                } catch (MqttException e) {
                    addLogEntry(LogEntry.LogLevel.ERROR, String.format("Could not connect to %s", brokerUrl));
                    Alert alert = new Alert(Alert.AlertType.ERROR, I18n.getString("broker.connection.error", brokerUrl));
                    alert.show();
                }


            });
        }
    }


    private void showMessagesForTopic(ClientTopic clientTopic) {
        if (clientTopic != null) {
            topicNameLabel.setText(clientTopic.getName());
            messagesList.clear();

            // Check if topic is formatted or not!
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

        // Do Not Block UI!
        Platform.runLater(() -> {
            if (arg instanceof ClientMessage) {
                addLogEntry(LogEntry.LogLevel.INFO, "Got a message from: "+((ClientMessage) arg).getTopicName());

                String topicName = ((ClientMessage) arg).getTopicName();

                ClientTopic topic = topicsMap.get(topicName);

                if (!topic.isFormattedTopic()) {
                    addLogEntry(LogEntry.LogLevel.INFO, "Topic was not formatted");
                    topic.getMessages().add((ClientMessage) arg);
                }
                else {
                    addLogEntry(LogEntry.LogLevel.INFO, "Topic was formatted");

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
