/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.drone.entities;

import com.amazonaws.s3.model.InvalidObjectStateException;
import io.github.dronesecurity.dronesystem.drone.application.drone.drone.MovingStatePublisherImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.services.MovingStatePublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities.SensorSet;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.DrivingMode;

/**
 * Item representing a drone with all its physical sensors.
 */
public class Drone {

    private final String id;
    private final SensorSet sensorSet;
    private DrivingMode drivingMode;
    private OrderData orderData;
    private boolean isMoving;

    private final MovingStatePublisher movingStatePublisher;

    /**
     * Constructs drone's sensors.
     * @param id drone identifier
     */
    public Drone(final String id) {
        this.id = id;
        this.sensorSet = new SensorSet();
        this.drivingMode = DrivingMode.AUTOMATIC;
        this.movingStatePublisher = new MovingStatePublisherImpl();
    }

    /**
     * Activates the Drone, making it operative.
     * @param currentOrderData data of the order that is being delivered
     */
    public void activate(final OrderData currentOrderData) {
        if (this.orderData == null) {
            this.orderData = currentOrderData;
            this.proceed();
            this.sensorSet.activate(this.orderData);
        }
    }

    /**
     * Deactivates the Drone, making it inoperative.
     */
    public void deactivate() {
        this.halt();
        this.sensorSet.deactivate();
    }

    /**
     * Checks if the Drone is operating (moving).
     * @return true if Drone is moving, false otherwise
     */
    public boolean isOperating() {
        return this.isMoving;
    }

    /**
     * Executes the analysis of the raw data of all sensors.
     */
    public void performReading() {
        final AlertLevel alertLevel = this.sensorSet.performReading();

        if (alertLevel == AlertLevel.CRITICAL && this.isOperating())
            this.halt();
    }

    /**
     * Publishes the data of all the drone's sensors.
     */
    public void publishSensorData() {
        this.sensorSet.publishData();
    }

    /**
     * Makes the Drone proceed with its delivery.
     */
    public void proceed() {
        if (this.drivingMode == DrivingMode.AUTOMATIC) {
            this.isMoving = true;
            if (this.orderData == null) throw InvalidObjectStateException.builder()
                    .message("Drone cannot operate with no order assigned.").build();
            this.movingStatePublisher.droneProceeding(this.orderData);
        }
    }

    /**
     * Halts the Drone.
     */
    public void halt() {
        this.isMoving = false;
        if (this.orderData == null) throw InvalidObjectStateException.builder()
                        .message("Drone cannot operate with no order assigned.").build();
        this.movingStatePublisher.droneHalted(this.orderData);
    }

    /**
     * Changes the current driving mode into the new.
     * @param newDrivingMode {@link DrivingMode} to apply
     */
    public void changeMode(final DrivingMode newDrivingMode) {
        this.drivingMode = newDrivingMode;
        if (this.drivingMode == DrivingMode.MANUAL)
            this.halt();
    }

    /**
     * Gets the drone identifier.
     * @return the drone identifier
     */
    public String getId() {
        return this.id;
    }
}
