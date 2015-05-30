package main.java.mqttclient.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Jan on 29.05.2015.
 */
public class KeyValuePair {

    @XmlElement(name="key")
    private String key;

    @XmlElement(name="value")
    private int value;

    private KeyValuePair() {}
    public KeyValuePair(String key, int value) {
        this.key = key;
        this.value = value;
    }

    @XmlTransient
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @XmlTransient
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
