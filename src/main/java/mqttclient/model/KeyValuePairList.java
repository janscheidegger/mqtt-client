package main.java.mqttclient.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by Jan on 29.05.2015.
 */
@XmlRootElement(name="keyValueList")
public class KeyValuePairList {


    @XmlElement(name="keyValuePair")
    private List<KeyValuePair> keyValuePairs;

    @XmlTransient
    public List<KeyValuePair> getKeyValuePairList() {
        return keyValuePairs;
    }

    public void setKeyValuePairList(List<KeyValuePair> keyValuePairs) {
        this.keyValuePairs = keyValuePairs;
    }
}
