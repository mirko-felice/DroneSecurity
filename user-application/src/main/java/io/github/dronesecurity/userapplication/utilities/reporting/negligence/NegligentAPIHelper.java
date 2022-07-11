/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.reporting.negligence;

import io.github.dronesecurity.userapplication.presentation.reporting.negligence.NegligentAPI;
import io.github.dronesecurity.userapplication.utilities.APIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.apache.commons.text.CaseUtils;

/**
 * API helper related to {@link NegligentAPI}.
 */
public final class NegligentAPIHelper {

    /**
     * Enum representing the different operations that can be performed over the
     * {@link NegligentAPI}.
     */
    public enum Operation {

        /**
         * Represents the operation to retrieve open reports for a negligent.
         */
        RETRIEVE_OPEN_REPORTS,

        /**
         * Represents the operation to retrieve closed reports for a negligent.
         */
        RETRIEVE_CLOSED_REPORTS;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return CaseUtils.toCamelCase(this.name(), false, '_');
        }
    }

    /**
     * Key to get/set the negligent.
     */
    public static final String NEGLIGENT_KEY = "negligent";

    private static final int PORT = 18_000;
    private static final String HOST = "localhost";
    private static final String BASE_URI = "/negligentAPI/";

    private NegligentAPIHelper() { }

    /**
     * Performs the HTTP Get method requesting a particular {@link Operation}.
     * @param operation {@link Operation} to perform on {@link NegligentAPI}
     * @param queryParamName name of the query param to add to request
     * @param queryParamValue value of the query param to add to request
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> get(final Operation operation,
                                                   final String queryParamName,
                                                   final String queryParamValue) {
        return APIHelper.getHTTP(PORT, HOST, BASE_URI + operation, queryParamName, queryParamValue, BodyCodec.buffer());
    }
}
