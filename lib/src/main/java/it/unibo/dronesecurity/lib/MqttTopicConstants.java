package it.unibo.dronesecurity.lib;

/**
 * Message exchange topic names.
 */
public final class MqttTopicConstants {

    /**
     * Sensor data exchange topic name.
     */
    public static final String DATA_TOPIC = "data";

    /**
     * Warning alert topic name.
     */
    public static final String WARNING_TOPIC = "warning";

    /**
     * Critical alert topic name.
     */
    public static final String CRITICAL_TOPIC = "critical";

    private MqttTopicConstants() { }
}