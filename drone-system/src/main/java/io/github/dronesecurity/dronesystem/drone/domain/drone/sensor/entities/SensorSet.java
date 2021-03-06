/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.entities;

import io.github.dronesecurity.dronesystem.drone.application.drone.sensor.AlertSituationAnalyzerImpl;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.AccelerometerAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.CameraAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.ProximityAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.SensorSetAlert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.exceptions.SensorNotActivatedException;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service.AlertSituationAnalyzer;
import io.github.dronesecurity.lib.shared.AlertLevel;
import io.github.dronesecurity.lib.shared.AlertType;
import io.github.dronesecurity.lib.utilities.CastHelper;

/**
 * Set of all sensors installed on the drone.
 */
public class SensorSet {

    private static final SensorFactory SENSOR_FACTORY = new SensorFactory();
    private final Sensor proximity;
    private final Sensor accelerometer;
    private final Sensor camera;
    private Alert currentProximityAlert;
    private Alert currentAccelerometerAlert;
    private OrderData orderData;

    private final AlertSituationAnalyzer alertAnalyzer;

    /**
     * Builds all the sensors that will be used during the delivery.
     */
    public SensorSet() {
        this.proximity = SENSOR_FACTORY.getProximitySensor();
        this.accelerometer = SENSOR_FACTORY.getAccelerometer();
        this.camera = SENSOR_FACTORY.getCamera();
        this.currentProximityAlert = new Alert(AlertType.DISTANCE, AlertLevel.STABLE);
        this.currentAccelerometerAlert = new Alert(AlertType.ANGLE, AlertLevel.STABLE);

        this.alertAnalyzer = new AlertSituationAnalyzerImpl();
    }

    /**
     * Activates all the sensors of the drone.
     * @param currentOrderData The data of the order currently being delivered
     */
    public void activate(final OrderData currentOrderData) {
        if (this.orderData == null) {
            this.orderData = currentOrderData;
            this.proximity.activate();
            this.accelerometer.activate();
            this.camera.activate();
        }
    }

    /**
     * Deactivates all the sensors of the drone.
     */
    public void deactivate() {
        this.proximity.deactivate();
        this.accelerometer.deactivate();
        this.camera.deactivate();
    }

    /**
     * Makes all the sensors of the drone perform a single reading.
     * @return the alert level detected after analyzing all the sensors
     */
    public SensorSetAlert performReading() {
        final ProximityAlert proximityAlert =
                CastHelper.safeCast(this.proximity.performReading(), ProximityAlert.class).orElseThrow();
        final AccelerometerAlert accelerometerAlert =
                CastHelper.safeCast(this.accelerometer.performReading(), AccelerometerAlert.class).orElseThrow();
        final CameraAlert cameraAlert =
                CastHelper.safeCast(this.camera.performReading(), CameraAlert.class).orElseThrow();

        final AlertLevel currentAlertLevel =
                this.alertAnalyzer.analyzeAlerts(this.orderData,
                        this.currentProximityAlert,
                        this.currentAccelerometerAlert,
                        proximityAlert,
                        accelerometerAlert);

        this.currentProximityAlert = proximityAlert;
        this.currentAccelerometerAlert = accelerometerAlert;

        return new SensorSetAlert(currentAlertLevel, proximityAlert, accelerometerAlert, cameraAlert);
    }

    /**
     * Publishes the data of the last reading performed by all the drone's sensors.
     */
    public void publishData() {
        if (this.orderData == null) throw new SensorNotActivatedException();
        this.proximity.publishData(this.orderData);
        this.accelerometer.publishData(this.orderData);
        this.camera.publishData(this.orderData);
    }
}
