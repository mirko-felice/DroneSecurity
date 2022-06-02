/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.utilities;

import io.github.dronesecurity.lib.MqttMessageParameterConstants;

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
     * Key for the camera data.
     */
    public static final String CAMERA = MqttMessageParameterConstants.CAMERA_PARAMETER;

    /**
     * Key for the closing instant.
     */
    public static final String CLOSING_INSTANT = "closingInstant";

    private NegligenceConstants() { }
}
