package main.java.mqttclient.observer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * mqqt-client
 *
 * @author jan
 */
public class Observable {
    private List<ObserverInfo> observers = new ArrayList<>();

    public void add(Object o) throws ObserverException {
        ObserverInfo observerInfo = null;
        for(Method method : o.getClass().getMethods()) {
            if (method.getAnnotation(Update.class) == null) continue;
            observerInfo = new ObserverInfo(method, o);
            observers.add(observerInfo);
        }
        if (observerInfo == null) {
            throw new ObserverException("Error");
        }

    }

    public void notifyObservers() {
        for(ObserverInfo observer : observers) {
            observer.invoke();
        }
    }
}
