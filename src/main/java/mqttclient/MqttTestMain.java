package main.java.mqttclient;


import main.java.mqttclient.mqtt.MqttAccessor;

/**
 * mqqt-client
 *
 * @author jan
 */
public class MqttTestMain {



    public static void main(String[] args) {
        MqttAccessor mqttAccessor = MqttAccessor.getInstance();


        mqttAccessor.subscribeTopic("tcp://iot.eclipse.org:1883", "jan/test", false);

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
