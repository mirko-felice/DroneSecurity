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
     * Current alert level topic name.
     */
    public static final String ALERT_LEVEL_TOPIC = "alertLevel";

    /**
     * Drone System and User Application synchronization topic name.
     */
    public static final String ORDER_TOPIC = "sync";

    /**
     * Drone System and User Application synchronization topic name.
     */
    public static final String LIFECYCLE_TOPIC = "lifecycle";

    /**
     * Negligence reports topic name.
     */
    public static final String NEGLIGENCE_REPORTS_TOPIC = "negligenceReports";

    private MqttTopicConstants() { }
}
