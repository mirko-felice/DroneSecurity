package it.unibo.dronesecurity.lib;

/**
 * Message JSON values.
 */
public final class MqttMessageValueConstants {

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

    /**
     * Drone delivering lifecycle message text.
     */
    public static final String DELIVERING_MESSAGE = "delivering";

    /**
     * Successful delivery message text.
     */
    public static final String DELIVERY_SUCCESSFUL_MESSAGE = "succeeded";

    /**
     * Failed delivery message text.
     */
    public static final String DELIVERY_FAILED_MESSAGE = "failed";

    /**
     * Drone returning lifecycle message text.
     */
    public static final String RETURN_ACKNOWLEDGEMENT_MESSAGE = "returning";

    /**
     * Perform delivery message text.
     */
    public static final String PERFORM_DELIVERY_MESSAGE = "perform delivery";

    /**
     * Drone callback message text.
     */
    public static final String DRONE_CALLBACK_MESSAGE = "callback";

    private MqttMessageValueConstants() { }
}
