package main.java.mqttclient;


import main.java.mqttclient.mqtt.MqttAccessor;

/**
 * mqqt-client
 *
 * @author jan
 */
public class MqttTestMain {



    public static void main(String[] args) {
        MqttAccessor mqttAccessor = new MqttAccessor("tcp://iot.eclipse.org:1883");


        mqttAccessor.subscribeTopic("jan/test");

        while(mqttAccessor.isCurrentlyListening()) {
            try {
                System.out.println(mqttAccessor.isCurrentlyListening());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mqttAccessor.disconnect();
        System.exit(0);

    }
}
