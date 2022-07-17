/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class that publishes delivery details to the {@link Connection}.
 */
public class DeliveryStatusPublisher {

    /**
     * Publish current drone status.
     * @param orderData order identifier needed in order to publish correctly
     * @param status status to publish
     */
    public void publishCurrentStatus(final @NotNull OrderData orderData, final String status) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.STATUS_PARAMETER, status);
        Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC + orderData.getOrderId(), payload);
    }
}
