package main.java.mqttclient;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.mqttclient.model.ClientMessage;
import main.java.mqttclient.model.ClientTopic;
import main.java.mqttclient.model.MessagesModel;
import main.java.mqttclient.view.MessagesController;

import java.io.IOException;

/**
 * Created by jan on 25/05/15.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    private Stage primaryStage;
    private BorderPane rootLayout;
    MessagesModel messagesModel = new MessagesModel();



    public Main() {
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MqttClient!");

        initRootLayout();
        showMessages();

        primaryStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                for(ClientTopic clientTopic : messagesModel.getClientTopicData()) {
                    clientTopic.getMqttAccessor().disconnect();
                }
            }
        });

    }

    public void showMessages() {
        // Give the controller access to the main app.
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MessagesView.fxml"));
            AnchorPane messageView = loader.load();

            rootLayout.setCenter(messageView);

            loader.<MessagesController>getController().setModel(messagesModel);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));

            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
