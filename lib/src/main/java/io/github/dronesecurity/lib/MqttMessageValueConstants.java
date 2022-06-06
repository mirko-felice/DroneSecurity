/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.lib;

/**
 * Message JSON values.
 */
public final class MqttMessageValueConstants {

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
     * Drone returned lifecycle message text.
     */
    public static final String RETURNED_ACKNOWLEDGEMENT_MESSAGE = "returned";

    /**
     * Perform delivery message text.
     */
    public static final String PERFORM_DELIVERY_MESSAGE = "perform delivery";

    /**
     * Drone call back message text.
     */
    public static final String DRONE_CALLBACK_MESSAGE = "callBack";

    /**
     * Drone automatic mode message text.
     */
    public static final String AUTOMATIC_MODE_MESSAGE = DrivingMode.AUTOMATIC.toString();

    /**
     * Drone manual mode message text.
     */
    public static final String MANUAL_MODE_MESSAGE = DrivingMode.MANUAL.toString();

    /**
     * Drone proceeding message text.
     */
    public static final String PROCEED_MESSAGE = "proceed";

    /**
     * Drone halting message text.
     */
    public static final String HALT_MESSAGE = "halt";

    private MqttMessageValueConstants() { }
}
