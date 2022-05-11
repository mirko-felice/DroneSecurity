package it.unibo.dronesecurity.userapplication.negligence.utilities;

import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;

/**
 *  Negligence related strings.
 */
public final class NegligenceConstants {

    /**
     * Key for the ID.
     */
    public static final String ID = "ID";

    /**
     * Key for the negligent.
     */
    public static final String NEGLIGENT = "negligent";

    /**
     * Key for the assignee.
     */
    public static final String ASSIGNEE = "assignee";

    /**
     * Key for collected data.
     */
    public static final String DATA = "data";

    /**
     * Key for proximity data.
     */
    public static final String PROXIMITY = MqttMessageParameterConstants.PROXIMITY_PARAMETER;

    /**
     * Key for accelerometer data.
     */
    public static final String ACCELEROMETER = MqttMessageParameterConstants.ACCELEROMETER_PARAMETER;

    /**
     * Key for the closing instant.
     */
    public static final String CLOSING_INSTANT = "closingInstant";

    private NegligenceConstants() { }
}
