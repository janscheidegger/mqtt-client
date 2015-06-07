package main.java.mqttclient;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.mqttclient.mqtt.MqttAccessor;

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



    public Main() {
    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MqttClient!");

        initRootLayout();
        showMessages();

        primaryStage.setOnHiding(event -> MqttAccessor.getInstance().disconnectAll());

    }

    public void showMessages() {
        // Give the controller access to the main app.
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MessagesView.fxml"));
            AnchorPane messageView = loader.load();

            rootLayout.setCenter(messageView);


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
            scene.getStylesheets().add(Main.class.getResource("view/theme.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
