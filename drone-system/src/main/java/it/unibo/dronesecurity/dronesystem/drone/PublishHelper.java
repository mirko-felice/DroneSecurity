package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Utility class to publish data and other information using {@link Connection}.
 */
public final class PublishHelper {

    private PublishHelper() { }

    /**
     * Publish all the data provided.
     * @param proximitySensorData proximity data
     * @param accelerometerSensorData accelerometer data
     * @param cameraSensorData camera data
     */
    public static void publishData(final Double proximitySensorData,
                                   final @NotNull Map<String, Double> accelerometerSensorData,
                                   final Double cameraSensorData) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximitySensorData);
        final ObjectNode accelerometerValues = mapper.createObjectNode();
        accelerometerSensorData.forEach(accelerometerValues::put);
        mapJson.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);
        mapJson.put(MqttMessageParameterConstants.CAMERA_PARAMETER, cameraSensorData);

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC, mapJson);
    }

    /**
     * Publish the current {@link AlertLevel} on the related topic.
     * @param currentAlertLevel current alert level to publish
     * @param type {@link AlertType} that causes the alert
     */
    public static void publishCurrentAlertLevel(final @NotNull AlertLevel currentAlertLevel,
                                                final @NotNull AlertType type) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER, String.valueOf(currentAlertLevel));
        payload.put(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER, type.toString());
        Connection.getInstance().publish(MqttTopicConstants.ALERT_LEVEL_TOPIC, payload);
    }

    /**
     * Publish current drone status.
     * @param status status to publish
     * @param currentOrderId order identifier needed in order to publish correctly
     */
    public static void publishCurrentStatus(final String status, final String currentOrderId) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ORDER_ID_PARAMETER, currentOrderId);
        payload.put(MqttMessageParameterConstants.STATUS_PARAMETER, status);
        Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC, payload);
    }
}
