/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.accelerometer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.objects.ProcessedAccelerometerData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.accelerometer.services.AccelerometerDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link AccelerometerDataPublisher} that publishes processed accelerometer data
 * to an established AWS connection.
 */
public class AccelerometerDataPublisherImpl implements AccelerometerDataPublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishAccelerometerData(final @NotNull OrderData orderData,
                                         final @NotNull ProcessedAccelerometerData accelerometerData) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        final ObjectNode accelerometerValues = mapper.createObjectNode();
        accelerometerData.asMap().forEach(accelerometerValues::put);
        mapJson.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, mapJson);
    }
}
