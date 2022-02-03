package it.unibo.dronesecurity.userapplication.drone.monitoring;

import io.vertx.core.json.JsonObject;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.userapplication.events.CriticalEvent;
import it.unibo.dronesecurity.userapplication.events.DataReadEvent;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.WarningEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    private static final String READING_TOPIC = "data";
    private static final String WARNING_TOPIC = "warning";
    private static final String CRITICAL_TOPIC = "critical";

    /**
     * Subscribes to the reading topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToDataReading(final DomainEvents<DataReadEvent> domainEvents) {
        Connection.getInstance().subscribe(READING_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            final double proximity = json.getDouble("proximity");

            final JsonObject accelerometerJson = json.getJsonObject("accelerometer");
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            accelerometer.put("x", accelerometerJson.getDouble("x"));
            accelerometer.put("y", accelerometerJson.getDouble("y"));
            accelerometer.put("z", accelerometerJson.getDouble("z"));

            final double camera = json.getDouble("camera");

            domainEvents.raise(new DataReadEvent(proximity, accelerometer, camera));
        });
    }

    /**
     * Subscribes to the warning topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToWarning(final DomainEvents<WarningEvent> domainEvents) {
        Connection.getInstance().subscribe(WARNING_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            domainEvents.raise(new WarningEvent(json.getString("message")));
        });
    }

    /**
     * Subscribes to the critical topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToCritical(final DomainEvents<CriticalEvent> domainEvents) {
        Connection.getInstance().subscribe(CRITICAL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload()));
            domainEvents.raise(new CriticalEvent(json.getString("message")));
        });
    }

    /**
     * Closes the active connection with AWS service.
     */
    public void closeConnection() {
        Connection.getInstance().closeConnection();
    }
}
