package it.unibo.dronesecurity.dronesystem.drone;

import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Sensor Data analyzer to check critical or warning situations.
 */
public final class DataAnalyzer {

    private static final double PROXIMITY_WARNING_THRESHOLD = 50;
    private static final double PROXIMITY_CRITICAL_THRESHOLD = 30;
    private static final double ACCELEROMETER_CRITICAL_THRESHOLD = 30;
    private static final double ACCELEROMETER_WARNING_THRESHOLD = 30;
    private static final double PI_IN_DEGREES = Math.toDegrees(Math.PI);

    /**
     * Checks if proximity distance is critical or warning giving back an {@link AlertLevel}.
     * @param proximityDistance distance collected by the {@link ProximitySensor}
     * @return an {@link AlertLevel}
     */
    public AlertLevel checkProximitySensorDataAlertLevel(final Double proximityDistance) {
        if (proximityDistance > 0) {
            final boolean isWarning = proximityDistance <= PROXIMITY_WARNING_THRESHOLD;
            final boolean isCritical = proximityDistance <= PROXIMITY_CRITICAL_THRESHOLD;
            if (isCritical)
                return AlertLevel.CRITICAL;
            else if (isWarning)
                return AlertLevel.WARNING;
        }
        return AlertLevel.NONE;
    }

    /**
     * Checks if inclination angle is critical or warning giving back an {@link AlertLevel}.
     * @param accelerometerData map of data collected by the {@link Accelerometer}
     * @return an {@link AlertLevel}
     */
    public AlertLevel checkAccelerometerDataAlertLevel(final @NotNull Map<String, Double> accelerometerData) {
        if (!accelerometerData.isEmpty()) {
            final double x = accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER);
            final double z = accelerometerData.get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER);
            final double yaw = Math.toDegrees(Math.atan2(z, x));

            final boolean isWarning = yaw > ACCELEROMETER_WARNING_THRESHOLD
                    && yaw < PI_IN_DEGREES - ACCELEROMETER_WARNING_THRESHOLD;

            final boolean isCritical = yaw > ACCELEROMETER_CRITICAL_THRESHOLD
                    && yaw < PI_IN_DEGREES - ACCELEROMETER_CRITICAL_THRESHOLD;

            if (isCritical)
                return AlertLevel.CRITICAL;
            else if (isWarning)
                return AlertLevel.WARNING;
        }
        return AlertLevel.NONE;
    }

}
