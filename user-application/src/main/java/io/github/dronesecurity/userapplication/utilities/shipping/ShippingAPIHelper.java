/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.shipping;

import io.github.dronesecurity.userapplication.presentation.shipping.ShippingAPI;
import io.github.dronesecurity.userapplication.utilities.APIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * API Helper related to the {@link ShippingAPI}.
 */
public final class ShippingAPIHelper {

    /**
     * Enum representing the different operations that can be performed over the
     * {@link ShippingAPI}.
     */
    public enum Operation {

        /**
         * Represents the operation to place an order.
         */
        PLACE_ORDER,

        /**
         * Represents the operation to list the orders.
         */
        LIST_ORDERS,

        /**
         * Represents the operation to perform a delivery.
         */
        PERFORM_DELIVERY,

        /**
         * Represents the operation to succeed the delivery.
         */
        SUCCEED_DELIVERY,

        /**
         * Represents the operation to fail the delivery.
         */
        FAIL_DELIVERY,

        /**
         * Represents the operation to reschedule a delivery.
         */
        RESCHEDULE_DELIVERY;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    // Place Order
    /**
     * Key to get/set the client name.
     */
    public static final String CLIENT_NAME_KEY = "clientName";

    /**
     * Key to get/set the product name.
     */
    public static final String PRODUCT_NAME_KEY = "productName";

    /**
     * Key to get/set the estimated arrival.
     */
    public static final String ESTIMATED_ARRIVAL_KEY = "estimatedArrival";

    // Perform delivery
    /**
     * Key to get/set the drone identifier.
     */
    public static final String DRONE_ID_KEY = "droneId";

    // Reschedule Delivery
    /**
     * Key to get/set the new estimated arrival.
     */
    public static final String NEW_ESTIMATED_ARRIVAL_KEY = "newEstimatedArrival";

    // Other operations
    /**
     * Key to get/set the order identifier.
     */
    public static final String ORDER_ID_KEY = "orderId";

    private static final int PORT = 15_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/shippingAPI/";

    private ShippingAPIHelper() { }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link ShippingAPI}
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> get(final Operation operation) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation, BodyCodec.buffer());
    }

    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link ShippingAPI}
     * @param json {@link JsonObject} to send as body
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final Operation operation, final @NotNull JsonObject json) {
        return APIHelper.postJson(PORT, HOST, BASE_URI + operation, json, BodyCodec.buffer());
    }
}
