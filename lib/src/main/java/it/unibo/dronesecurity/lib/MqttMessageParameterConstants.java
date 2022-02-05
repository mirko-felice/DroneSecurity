package it.unibo.dronesecurity.lib;

/**
 * Message JSON parameters.
 */
public final class MqttMessageParameterConstants {

    /**
     * Parameter that contains proximity data.
     */
    public static final String PROXIMITY_PARAMETER = "proximity";

    /**
     * Parameter that contains accelerometer data.
     */
    public static final String ACCELEROMETER_PARAMETER = "accelerometer";

    /**
     * Parameter that contains camera data.
     */
    public static final String CAMERA_PARAMETER = "camera";

    /**
     * Parameter that contains accelerometer X value.
     */
    public static final String ACCELEROMETER_X_PARAMETER = "x";

    /**
     * Parameter that contains accelerometer Y value.
     */
    public static final String ACCELEROMETER_Y_PARAMETER = "y";

    /**
     * Parameter that contains accelerometer Z value.
     */
    public static final String ACCELEROMETER_Z_PARAMETER = "z";

    /**
     * Parameter that contains dangerous situation messages.
     */
    public static final String MESSAGE_PARAMETER = "message";

    /**
     * Parameter that contains drone lifecycle updates.
     */
    public static final String STATUS_PARAMETER = "status";

    private MqttMessageParameterConstants() { }
}
