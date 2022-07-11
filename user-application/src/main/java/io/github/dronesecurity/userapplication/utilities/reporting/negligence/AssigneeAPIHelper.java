/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.reporting.negligence;

import io.github.dronesecurity.userapplication.presentation.reporting.negligence.AssigneeAPI;
import io.github.dronesecurity.userapplication.utilities.APIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * API helper related to {@link AssigneeAPI}.
 */
public final class AssigneeAPIHelper {

    /**
     * Enum representing the different operations that can be performed over the
     * {@link AssigneeAPI}.
     */
    public enum Operation {

        /**
         * Represents the operation to retrieve open reports for an assignee.
         */
        RETRIEVE_OPEN_REPORTS,

        /**
         * Represents the operation to retrieve closed reports for an assignee.
         */
        RETRIEVE_CLOSED_REPORTS,

        /**
         * Represents the operation to take action regarding a negligence report.
         */
        TAKE_ACTION;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    /**
     * Key to get/set the assignee.
     */
    public static final String ASSIGNEE_KEY = "assignee";

    /**
     * Key to get/set the report.
     */
    public static final String REPORT_KEY = "report";

    /**
     * Key to get/set the action form.
     */
    public static final String ACTION_FORM_KEY = "solution";

    private static final int PORT = 19_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/assigneeAPI/";

    private AssigneeAPIHelper() { }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link AssigneeAPI}
     * @param queryParamName name of the query param to add to request
     * @param queryParamValue value of the query param to add to request
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> get(final Operation operation,
                                                   final String queryParamName,
                                                   final String queryParamValue) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation, queryParamName, queryParamValue, BodyCodec.buffer());
    }

    /**
     * Performs the HTTP Post method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link AssigneeAPI}
     * @param json {@link JsonObject} to send as body
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final Operation operation,
                                                        final @NotNull JsonObject json) {
        return APIHelper.postJson(PORT, HOST, BASE_URI + operation, json, BodyCodec.buffer());
    }
}
