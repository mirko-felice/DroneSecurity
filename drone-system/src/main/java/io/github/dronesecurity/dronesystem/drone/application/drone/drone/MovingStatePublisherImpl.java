/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.drone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.drone.services.MovingStatePublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttMessageValueConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link MovingStatePublisher} that publishes data to AWS connection
 * when the drone is halted or proceeds with the delivery.
 */
public class MovingStatePublisherImpl implements MovingStatePublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void droneHalted(final OrderData orderData) {
        this.publishMovingState(orderData, MqttMessageValueConstants.DRONE_STOPPED_STATE_MESSAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void droneProceeding(final OrderData orderData) {
        this.publishMovingState(orderData, MqttMessageValueConstants.DRONE_MOVING_STATE_MESSAGE);
    }

    private void publishMovingState(final @NotNull OrderData orderData, final String movingState) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.DRONE_MOVING_STATE_PARAMETER, movingState);
        Connection.getInstance().publish(MqttTopicConstants.DRONE_MOVING_TOPIC + orderData.getOrderId(), payload);
    }
}
