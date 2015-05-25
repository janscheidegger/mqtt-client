package main.java.mqttclient.observer;

/**
 * mqqt-client
 *
 * @author jan
 */
public class ObserverException extends Exception {
    private String message;

    public ObserverException() {
        super();
    }
    public ObserverException(String message) {
        super(message);
    }

    public ObserverException(String message, Throwable cause) {
        super(message, cause);
    }
    public ObserverException(Throwable cause) {
        super(cause);
    }
}
