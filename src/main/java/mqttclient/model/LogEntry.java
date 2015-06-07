package main.java.mqttclient.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * mqtt-client
 *
 * @author jan
 */
public class LogEntry {

    public enum LogLevel {
        ERROR, WARNING, INFO, DEBUG
    }

    private ObjectProperty<LogLevel> logLevel;
    private StringProperty message;
    private ObjectProperty<LocalDate> time;

    public LogEntry(LogLevel logLevel, String message) {
        this.time = new SimpleObjectProperty<>(LocalDate.now());
        this.logLevel = new SimpleObjectProperty<>(logLevel);
        this.message = new SimpleStringProperty(message);
    }

    public LogLevel getLogLevel() {
        return logLevel.get();
    }

    public ObjectProperty<LogLevel> logLevelProperty() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel.set(logLevel);
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public LocalDate getTime() {
        return time.get();
    }

    public ObjectProperty<LocalDate> timeProperty() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time.set(time);
    }
}
