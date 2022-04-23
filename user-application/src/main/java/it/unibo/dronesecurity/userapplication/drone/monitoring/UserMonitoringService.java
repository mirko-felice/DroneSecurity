package it.unibo.dronesecurity.userapplication.drone.monitoring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import it.unibo.dronesecurity.lib.*;
import it.unibo.dronesecurity.userapplication.events.*;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that monitors drone data channels.
 */
public final class UserMonitoringService {

    /**
     * Subscribes to the reading topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToDataRead(final DomainEvents<DataRead> domainEvents) {
        Connection.getInstance().subscribe(MqttTopicConstants.DATA_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            final double proximity = json.getDouble(MqttMessageParameterConstants.PROXIMITY_PARAMETER);

            final JsonObject accelerometerJson =
                    json.getJsonObject(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER);
            final Map<String, Double> accelerometer = new ConcurrentHashMap<>();
            accelerometer.put(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER));
            accelerometer.put(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER));
            accelerometer.put(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER,
                    accelerometerJson.getDouble(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER));

            final double camera = json.getDouble(MqttMessageParameterConstants.CAMERA_PARAMETER);

            domainEvents.raise(new DataRead(proximity, accelerometer, camera));
        });
    }

    /**
     * Subscribes to the warning situations.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToWarningSituation(final DomainEvents<WarningSituation> domainEvents) {
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.WARNING.toString()))
                domainEvents.raise(
                        new WarningSituation(json.getString(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER)));
        });
    }

    /**
     * Subscribes to the critical situations.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToCriticalSituation(final DomainEvents<CriticalSituation> domainEvents) {
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.CRITICAL.toString()))
                domainEvents.raise(
                        new CriticalSituation(json.getString(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER)));
        });
    }

    /**
     * Subscribes to the standard situations.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToStandardSituation(final DomainEvents<StandardSituation> domainEvents) {
        Connection.getInstance().subscribe(MqttTopicConstants.ALERT_LEVEL_TOPIC, msg -> {
            final JsonObject json = new JsonObject(new String(msg.getPayload(), StandardCharsets.UTF_8));
            if (json.getString(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER)
                    .equals(AlertLevel.NONE.toString()))
                domainEvents.raise(new StandardSituation());
        });
    }
    /**
     * Subscribes to drone status topic.
     *
     * @param domainEvents Domain to raise events on
     */
    public void subscribeToOrderStatusChange(final DomainEvents<StatusChanged> domainEvents) {
        Connection.getInstance().subscribe(MqttTopicConstants.LIFECYCLE_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final String status = json.get(MqttMessageParameterConstants.STATUS_PARAMETER).asText();
                domainEvents.raise(new StatusChanged(status));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
            }
        });
    }
}
