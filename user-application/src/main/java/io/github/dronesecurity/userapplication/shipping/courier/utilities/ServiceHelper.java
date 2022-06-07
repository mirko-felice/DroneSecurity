/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttMessageValueConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class dedicated to communication between controllers and the
 * {@link io.github.dronesecurity.userapplication.shipping.courier.CourierShippingService} and for the latter itself.
 */
public final class ServiceHelper {

    /**
     * Key to get the courier username.
     */
    public static final String COURIER_KEY = "courier";

    /**
     * Key to get the complete order.
     */
    public static final String ORDER_KEY = "order";

    /**
     * Key to get the order identifier.
     */
    public static final String ORDER_ID_KEY = "orderId";

    /**
     * Key to get the order state.
     */
    public static final String STATE_KEY = "state";

    /**
     * Key to get the drone identifier.
     */
    public static final String DRONE_ID_KEY = "droneId";

    /**
     * Key to get the new estimated arrival.
     */
    public static final String NEW_ESTIMATED_ARRIVAL_KEY = "newEstimatedArrival";

    /**
     * Represents the success of the delivery.
     */
    public static final String DELIVERY_SUCCESSFUL = MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE;

    /**
     * Represents the failure of the delivery.
     */
    public static final String DELIVERY_FAILED = MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE;

    private static final int PORT = 15_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/courierShippingService/";

    /**
     * Enum representing the different operations that can be performed over the
     * {@link io.github.dronesecurity.userapplication.shipping.courier.CourierShippingService}.
     */
    public enum Operation {

        /**
         * Represents the operation to perform a delivery.
         */
        PERFORM_DELIVERY,

        /**
         * Represents the operation to reschedule a delivery.
         */
        RESCHEDULE_DELIVERY,

        /**
         * Represents the operation to list the orders.
         */
        LIST_ORDERS,

        /**
         * Represents the operation to call back the drone.
         */
        CALL_BACK,

        /**
         * Represents the operation to save the delivery state.
         */
        SAVE_DELIVERY;

        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    private ServiceHelper() { }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on the
     * {@link io.github.dronesecurity.userapplication.shipping.courier.CourierShippingService}
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> getOperation(final @NotNull Operation operation) {
        return VertxHelper.WEB_CLIENT.get(PORT, HOST, BASE_URI + operation).send();
    }


    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on the
     * {@link io.github.dronesecurity.userapplication.shipping.courier.CourierShippingService}
     * @param json {@link JsonObject} to send as body
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final @NotNull Operation operation,
                                                        final @NotNull JsonObject json) {
        return VertxHelper.WEB_CLIENT.post(PORT, HOST, BASE_URI + operation)
                .putHeader("Content-Type", "application/json")
                .sendBuffer(json.toBuffer());
    }

    /**
     * Sends the message to perform the delivery on AWS.
     * @param droneId drone identifier to send message to
     * @param orderId order identifier to track
     * @param courier courier username to track
     */
    public static void sendPerformDeliveryMessage(final String droneId, final String orderId, final String courier) {
        final JsonNode messageJson = new ObjectMapper().createObjectNode()
                .put(MqttMessageParameterConstants.SYNC_PARAMETER, MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE)
                .put(MqttMessageParameterConstants.ORDER_ID_PARAMETER, orderId)
                .put(MqttMessageParameterConstants.COURIER_PARAMETER, courier);
        Connection.getInstance().publish(MqttTopicConstants.ORDER_TOPIC + droneId, messageJson);
    }

    /**
     * Sends the message to call back the drone on AWS.
     * @param orderId drone identifier to track
     */
    public static void sendCallBackMessage(final String orderId) {
        final JsonNode recallMessage = new ObjectMapper().createObjectNode()
                .put(MqttMessageParameterConstants.SYNC_PARAMETER,
                        MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.ORDER_TOPIC + orderId, recallMessage);
    }
}
