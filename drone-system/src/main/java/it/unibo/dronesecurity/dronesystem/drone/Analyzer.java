package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageValueConstants;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Sensor Data analyzer to check critical or warning situations.
 */
public final class Analyzer {

    private static final double PROXIMITY_WARNING_THRESHOLD = 50;
    private static final double PROXIMITY_CRITICAL_THRESHOLD = 30;
    private static final double ACCELEROMETER_CRITICAL_THRESHOLD = 30;
    private static final double ACCELEROMETER_WARNING_THRESHOLD = 30;
    private static final double PI_IN_DEGREES = Math.toDegrees(Math.PI);

    /**
     * Checks if proximity distance is critical or warning and eventually publish the alert.
     * @param proximityDistance distance collected by the {@link ProximitySensor}
     * @return true if proximity distance is critical, false otherwise
     */
    public boolean isProximityCritical(final Double proximityDistance) {
        if (proximityDistance > 0) {
            final boolean isWarning = proximityDistance <= PROXIMITY_WARNING_THRESHOLD;
            final boolean isCritical = proximityDistance <= PROXIMITY_CRITICAL_THRESHOLD;
            if (isCritical)
                this.publishAlert(MqttTopicConstants.CRITICAL_TOPIC,
                        MqttMessageValueConstants.CRITICAL_PROXIMITY_MESSAGE);
            else if (isWarning)
                this.publishAlert(MqttTopicConstants.WARNING_TOPIC,
                        MqttMessageValueConstants.WARNING_PROXIMITY_MESSAGE);
        }
        return false;
    }

    /**
     * Checks if inclination angle is critical or warning and eventually publish the alert.
     * @param accelerometerData map of data collected by the {@link Accelerometer}
     * @return true if inclination angle is critical, false otherwise
     */
    public boolean isCriticalInclinationAngle(final @NotNull Map<String, Double> accelerometerData) {
        if (!accelerometerData.isEmpty()) {
            final double x = accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER);
            final double z = accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER);
            final double yaw = Math.toDegrees(Math.atan2(z, x));

            final boolean isWarning = yaw > ACCELEROMETER_WARNING_THRESHOLD
                    && yaw < PI_IN_DEGREES - ACCELEROMETER_WARNING_THRESHOLD;

            final boolean isCritical = yaw > ACCELEROMETER_CRITICAL_THRESHOLD
                    && yaw < PI_IN_DEGREES - ACCELEROMETER_CRITICAL_THRESHOLD;

            if (isCritical)
                this.publishAlert(MqttTopicConstants.CRITICAL_TOPIC,
                        MqttMessageValueConstants.CRITICAL_ANGLE_MESSAGE);
            else if (isWarning)
                this.publishAlert(MqttTopicConstants.WARNING_TOPIC,
                        MqttMessageValueConstants.WARNING_ANGLE_MESSAGE);
        }
        return false;
    }

    private void publishAlert(final String topic, final @NotNull String message) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.MESSAGE_PARAMETER, message);
        Connection.getInstance().publish(topic, payload);
    }
}
