package main.java.mqttclient.observer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * mqqt-client
 *
 * @author jan
 */
public class ObserverInfo {
    final Method method;
    final Object object;

    public ObserverInfo(Method method, Object object) {
        this.method = method;
        this.object = object;
    }

    void invoke() {
        try {
            method.invoke(object);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
