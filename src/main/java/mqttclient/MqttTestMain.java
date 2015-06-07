package main.java.mqttclient;


import main.java.mqttclient.mqtt.MqttAccessor;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * mqqt-client
 *
 * @author jan
 */
public class MqttTestMain {



    public static void main(String[] args) {
        MqttAccessor mqttAccessor = MqttAccessor.getInstance();


        try {
            mqttAccessor.subscribeTopic("tcp://iot.eclipse.org:1883", "jan/test", false);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        while(mqttAccessor.isCurrentlyListening()) {
            try {
                System.out.println(mqttAccessor.isCurrentlyListening());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mqttAccessor.disconnectAll();
        System.exit(0);

    }
}
