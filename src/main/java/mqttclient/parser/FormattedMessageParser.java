package main.java.mqttclient.parser;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import main.java.mqttclient.model.KeyValuePairList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.StringReader;

/**
 * Created by jan on 30/05/15.
 */
public class FormattedMessageParser {

    private JAXBContext jc;

    public FormattedMessageParser() {
        this.jc = null;
        try {
            this.jc = JAXBContext.newInstance(KeyValuePairList.class);
        } catch (JAXBException e) {
            System.err.println(KeyValuePairList.class.getName() + " is not a valid class to Create a JAXBContext");
        }
    }

    public KeyValuePairList parseString(String stringToParse){
        try {
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (KeyValuePairList)unmarshaller.unmarshal(new StringReader(stringToParse));
        } catch (JAXBException e) {
            System.err.println("Could not create Unmarshaller");
            e.printStackTrace();
        }
        return null;
    }
}
