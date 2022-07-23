/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.shipping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.connection.MqttMessageValueConstants;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.lib.shared.DrivingMode;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * Helper dedicated to AWS message application logic.
 */
public final class ConnectionHelper {

    private ConnectionHelper() { }

    /**
     * Sends the message to perform the delivery on AWS.
     * @param droneId drone identifier to send message to
     * @param order {@link Order} sent to the drone
     * @param courier courier username to track
     */
    public static void sendPerformDeliveryMessage(final String droneId,
                                                  final @NotNull Order order,
                                                  final String courier) {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode messageJson = mapper.createObjectNode()
                .put(MqttMessageParameterConstants.SYNC_PARAMETER, MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE)
                .put(MqttMessageParameterConstants.COURIER_PARAMETER, courier)
                .set(MqttMessageParameterConstants.ORDER_PARAMETER, mapper.convertValue(order, JsonNode.class));
        Connection.getInstance().publish(MqttTopicConstants.ORDER_TOPIC + droneId, messageJson);
    }

    /**
     * Sends the message to call back the drone on AWS.
     * @param orderId {@link OrderIdentifier} used to identify the drone
     */
    public static void sendCallBackMessage(final @NotNull OrderIdentifier orderId) {
        final JsonNode recallMessage = new ObjectMapper().createObjectNode()
                .put(MqttMessageParameterConstants.SYNC_PARAMETER,
                        MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.ORDER_TOPIC + orderId.asLong(), recallMessage);
    }

    /**
     * Sends the message to change driving mode of the drone.
     * @param orderId {@link OrderIdentifier} used to identify the drone
     * @param drivingMode {@link DrivingMode} to set
     */
    public static void sendChangeModeMessage(final @NotNull OrderIdentifier orderId, final DrivingMode drivingMode) {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        final String modeMessage = drivingMode == DrivingMode.AUTOMATIC
                ? MqttMessageValueConstants.AUTOMATIC_MODE_MESSAGE
                : MqttMessageValueConstants.MANUAL_MODE_MESSAGE;
        jsonNode.put(MqttMessageParameterConstants.MODE_PARAMETER, modeMessage);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + orderId.asLong(), jsonNode);
    }

    /**
     * Sends the message to tell drone to proceed.
     * @param orderId {@link OrderIdentifier} used to identify the drone
     */
    public static void sendProceedMessage(final @NotNull OrderIdentifier orderId) {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put(MqttMessageParameterConstants.MOVE_PARAMETER, MqttMessageValueConstants.PROCEED_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + orderId.asLong(), jsonNode);
    }

    /**
     * Sends the message to tell drone to halt.
     * @param orderId {@link OrderIdentifier} used to identify the drone
     */
    public static void sendHaltMessage(final @NotNull OrderIdentifier orderId) {
        final ObjectNode jsonNode = new ObjectMapper().createObjectNode();
        jsonNode.put(MqttMessageParameterConstants.MOVE_PARAMETER, MqttMessageValueConstants.HALT_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.CONTROL_TOPIC + orderId.asLong(), jsonNode);
    }
}
