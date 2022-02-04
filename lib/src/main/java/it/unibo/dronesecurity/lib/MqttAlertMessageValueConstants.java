package it.unibo.dronesecurity.lib;

/**
 * Alert message values.
 */
public final class MqttAlertMessageValueConstants {

    /**
     * Critical distance message text.
     */
    public static final String CRITICAL_PROXIMITY_MESSAGE = "Critical distance!";

    /**
     * Warning distance message text.
     */
    public static final String WARNING_PROXIMITY_MESSAGE = "Watch out the distance!";

    /**
     * Critical inclination message text.
     */
    public static final String CRITICAL_ANGLE_MESSAGE = "Critical angle!";

    /**
     * Warning inclination message text.
     */
    public static final String WARNING_ANGLE_MESSAGE = "Watch out the angle!";

    private MqttAlertMessageValueConstants() { }
}
