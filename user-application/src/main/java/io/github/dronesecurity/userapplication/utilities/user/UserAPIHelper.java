/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.user;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.presentation.user.UserAPI;
import io.github.dronesecurity.userapplication.utilities.APIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * API helper related to {@link UserAPI}.
 */
public final class UserAPIHelper {

    /**
     * Enum representing the different operations that can be performed over the
     * {@link UserAPI}.
     */
    public enum Operation {

        /**
         * Represents the operation to log into the system.
         */
        LOG_IN,

        /**
         * Represents the operation to log out of the system.
         */
        LOG_OUT,

        /**
         * Represents the operation to retrieve a {@link Courier} from his username.
         */
        RETRIEVE_COURIER_BY_USERNAME,

        /**
         * Represents the operation to retrieve a {@link Maintainer} from his username.
         */
        RETRIEVE_MAINTAINER_BY_USERNAME,

        /**
         * Represents the operation to retrieve the logged user {@link Role} if present.
         */
        CHECK_LOGGED_USER_ROLE,

        /**
         * Represents the operation to retrieve the logged {@link Courier} if present.
         */
        RETRIEVE_LOGGED_COURIER_IF_PRESENT,

        /**
         * Represents the operation to retrieve the logged {@link Maintainer} if present.
         */
        RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,

        /**
         * Represents the operation to retrieve the couriers supervised by the logged {@link Maintainer}.
         */
        RETRIEVE_COURIERS_SUPERVISED_BY_LOGGED_MAINTAINER,

        /**
         * Represents the operation to add a drone on the logged {@link Courier}.
         */
        ADD_DRONE,

        /**
         * Represents the operation to remove a drone from the logged {@link Courier}.
         */
        REMOVE_DRONE;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    /**
     * Key to get/set the username.
     */
    public static final String USERNAME_KEY = "username";

    /**
     * Key to get/set the password.
     */
    public static final String PASSWORD_KEY = "password";

    /**
     * Key to get/set the drone identifier.
     */
    public static final String DRONE_KEY = "droneId";

    private static final int PORT = 17_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/userAPI/";

    private UserAPIHelper() { }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link UserAPI}
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> get(final Operation operation, final BodyCodec<T> bodyCodec) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation, bodyCodec);
    }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link UserAPI}
     * @param queryParamName name of the query param to add to request
     * @param queryParamValue value of the query param to add to request
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> get(final Operation operation,
                                                  final String queryParamName,
                                                  final String queryParamValue,
                                                  final BodyCodec<T> bodyCodec) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation, queryParamName, queryParamValue, bodyCodec);
    }

    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link UserAPI}
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final Operation operation) {
        return APIHelper.postJson(PORT, HOST, BASE_URI + operation, BodyCodec.buffer());
    }

    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link UserAPI}
     * @param json {@link JsonObject} to send as body
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final Operation operation, final @NotNull JsonObject json) {
        return APIHelper.postJson(PORT, HOST, BASE_URI + operation, json, BodyCodec.buffer());
    }

}
