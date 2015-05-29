package main.java.mqttclient.model;


import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Jan on 29.05.2015.
 */
public class KeyValuePair {

    @XmlElement(name="key")
    private String key;

    @XmlElement(name="value")
    private String value;

    private KeyValuePair() {}
    public KeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
