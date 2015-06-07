package main.java.mqttclient.model;

import org.junit.Test;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jan on 29.05.2015.
 */
public class KeyValuePairListTest {

    @Test
    public void testKeyValuePairXml() throws Exception {
        JAXBContext jc;

        List<KeyValuePair> keyValuePairs = new ArrayList<KeyValuePair>();
        keyValuePairs.add(new KeyValuePair("Key", 1));
        keyValuePairs.add(new KeyValuePair("Key2", 2));
        keyValuePairs.add(new KeyValuePair("Key3", 3));

        KeyValuePairList kvpList = new KeyValuePairList();
        kvpList.setKeyValuePairList(keyValuePairs);

        try {
            jc = JAXBContext.newInstance(KeyValuePairList.class);

            Marshaller marshaller = jc.createMarshaller();
            File xmlDocument = new File("output.xml");
            marshaller.marshal(kvpList, new FileOutputStream(xmlDocument));
            System.out.println(xmlDocument);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}