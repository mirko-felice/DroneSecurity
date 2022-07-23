/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.accelerometer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.objects.ProcessedAccelerometerPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.accelerometer.services.AccelerometerPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the {@link AccelerometerPerformancePublisher}.
 */
public class AccelerometerPerformancePublisherImpl implements AccelerometerPerformancePublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishAccelerometer(
            final @NotNull ProcessedAccelerometerPerformanceData accelerometerPerformanceData) {

        final ObjectNode accelerometerJson = new ObjectMapper().createObjectNode();
        final ProcessedAccelerometerData accelerometerData = accelerometerPerformanceData.getAccelerometerData();
        accelerometerJson.put(MqttMessageParameterConstants.PITCH, accelerometerData.getPitch());
        accelerometerJson.put(MqttMessageParameterConstants.ROLL, accelerometerData.getRoll());
        accelerometerJson.put(MqttMessageParameterConstants.YAW, accelerometerData.getYaw());
        accelerometerJson.put(PerformanceStringConstants.TIMESTAMP, accelerometerPerformanceData.getTimestamp());
        accelerometerJson.put(PerformanceStringConstants.INDEX, accelerometerPerformanceData.getIndex());

        Connection.getInstance().publish(
                MqttTopicConstants.PERFORMANCE_TOPIC + " " + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER,
                accelerometerJson);
    }
}
