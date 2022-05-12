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
     * Parameter that contains synchronization message.
     */
    public static final String SYNC_PARAMETER = "sync";

    /**
     * Parameter that contains current {@link AlertLevel}.
     */
    public static final String ALERT_LEVEL_PARAMETER = "level";

    /**
     * Parameter that contains alert type.
     */
    public static final String ALERT_TYPE_PARAMETER = "type";

    /**
     * Parameter that contains drone lifecycle updates.
     */
    public static final String STATUS_PARAMETER = "status";

    /**
     * Parameter that contains order identifier.
     */
    public static final String ORDER_ID_PARAMETER = "orderId";

    /**
     * Parameter that contains issue report details.
     */
    public static final String ISSUE_REPORT_INFO_PARAMETER = "details";

    /**
     * Parameter that contains courier leading the order.
     */
    public static final String COURIER_PARAMETER = "courier";

    /**
     * Parameter that contains negligent for a Negligence Report.
     */
    public static final String NEGLIGENT_PARAMETER = "negligent";

    private MqttMessageParameterConstants() { }
}
