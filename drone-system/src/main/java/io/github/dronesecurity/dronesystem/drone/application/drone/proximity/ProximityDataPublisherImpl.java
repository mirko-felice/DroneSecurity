/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.proximity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.objects.ProcessedProximityData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.proximity.services.ProximityDataPublisher;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link ProximityDataPublisher} that publishes processed proximity sensor data
 * to an established AWS connection.
 */
public class ProximityDataPublisherImpl implements ProximityDataPublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishProximityData(final @NotNull OrderData orderData,
                                     final @NotNull ProcessedProximityData proximityData) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximityData.getDistance());

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.PROXIMITY_PARAMETER, mapJson);
    }
}
