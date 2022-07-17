/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities;

import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;

/**
 * Class representing a generic Sensor that returns a data of R type.
 */
public interface Sensor {

    /**
     * Activates this sensor.
     */
    void activate();

    /**
     * Deactivates this sensor.
     */
    void deactivate();

    /**
     * Informs if the sensor is active or not.
     *
     * @return The sensor active or not status
     */
    boolean isOn();

    /**
     * Reads data from its sensor and processes it accordingly.
     * @return alert result of the current reading
     */
    Alert performReading();

    /**
     * Publishes sensor's data.
     * @param orderData The order the is currently being delivered
     */
    void publishData(OrderData orderData);
}
