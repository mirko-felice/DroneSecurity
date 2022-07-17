/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.alert.objects.Alert;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.sensor.service.AlertLevelPublisher;
import io.github.dronesecurity.lib.AlertLevel;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link AlertLevelPublisher} that publishes the current {@link Alert} situation of the drone
 * to the AWS topic.
 */
public class AlertLevelPublisherImpl implements AlertLevelPublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishCurrentAlertLevel(final @NotNull OrderData orderData, final @NotNull Alert currentAlert) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER, String.valueOf(currentAlert.getAlertLevel()));
        payload.put(MqttMessageParameterConstants.ALERT_TYPE_PARAMETER, currentAlert.getAlertType().toString());
        Connection.getInstance().publish(MqttTopicConstants.ALERT_LEVEL_TOPIC + orderData.getOrderId(), payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishStableAlertLevel(final @NotNull OrderData orderData) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ALERT_LEVEL_PARAMETER, AlertLevel.STABLE.toString());
        Connection.getInstance().publish(MqttTopicConstants.ALERT_LEVEL_TOPIC + orderData.getOrderId(), payload);
    }
}
