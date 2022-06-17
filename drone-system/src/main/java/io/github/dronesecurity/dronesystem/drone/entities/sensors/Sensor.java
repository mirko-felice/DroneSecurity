/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.entities.sensors;

/**
 * Class representing a generic Sensor that returns a data of R type.
 *
 * @param <SensorData> type of sensor data
 */
public interface Sensor<SensorData> {

    /**
     * Informs if the sensor is active or not.
     *
     * @return The sensor active or not status
     */
    boolean isOn();

    /**
     * Activates this sensor.
     */
    void activate();

    /**
     * Deactivates this sensor.
     */
    void deactivate();

    /**
     * Analyzes last raw data detected and transforms it in readable values.
     */
    void readData();

    /**
     * Gives the last readable data obtained by the raw.
     *
     * @return Readable values obtained by the last analysis
     */
    SensorData getData();
}
