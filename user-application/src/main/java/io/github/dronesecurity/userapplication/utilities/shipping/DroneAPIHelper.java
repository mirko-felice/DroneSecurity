/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.shipping;

import io.github.dronesecurity.userapplication.presentation.shipping.DroneAPI;
import io.github.dronesecurity.userapplication.utilities.APIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * API Helper related to the {@link DroneAPI}.
 */
public final class DroneAPIHelper {

    /**
     * Key to get/set the order identifier.
     */
    public static final String ORDER_ID_KEY = "orderId";

    /**
     * Key to get/set the driving mode of the drone to apply.
     */
    public static final String DRIVING_MODE_KEY = "drivingMode";

    private static final int PORT = 16_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/droneAPI/";

    private DroneAPIHelper() { }

    /**
     * Enum representing the different operations that can be performed over the
     * {@link DroneAPI}.
     */
    public enum Operation {

        /**
         * Represents the operation to call back the drone.
         */
        CALL_BACK,

        /**
         * Represents the operation to change driving mode of the drone.
         */
        CHANGE_MODE,

        /**
         * Represents the operation to tell drone to proceed.
         */
        PROCEED,

        /**
         * Represents the operation to tell drone to halt.
         */
        HALT;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link DroneAPI}
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> get(final Operation operation) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation);
    }

    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link DroneAPI}
     * @param json {@link JsonObject} to send as body
     */
    public static void postJson(final Operation operation, final @NotNull JsonObject json) {
        APIHelper.postJson(PORT, HOST, BASE_URI + operation, json);
    }
}
