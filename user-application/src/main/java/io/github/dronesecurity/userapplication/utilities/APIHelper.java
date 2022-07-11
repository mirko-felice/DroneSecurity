/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import org.jetbrains.annotations.NotNull;

/**
 * Generic HTTP API Helper.
 */
public final class APIHelper {

    private APIHelper() { }

    /**
     * Performs the HTTP Get method request using parameters.
     * @param port port of the server to send the request
     * @param host host address of the server to send the request
     * @param uri complete uri of the API to send the request
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> getHTTP(final int port,
                                                      final String host,
                                                      final String uri,
                                                      final BodyCodec<T> bodyCodec) {
        return VertxHelper.WEB_CLIENT.get(port, host, uri).as(bodyCodec).send();
    }

    /**
     * Performs the HTTP Get method request using parameters.
     * @param port port of the server to send the request
     * @param host host address of the server to send the request
     * @param uri complete uri of the API to send the request
     * @param queryParamName name of the query param to add to request
     * @param queryParamValue value of the query param to add to request
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> getHTTP(final int port,
                                                      final String host,
                                                      final String uri,
                                                      final String queryParamName,
                                                      final String queryParamValue,
                                                      final BodyCodec<T> bodyCodec) {
        return VertxHelper.WEB_CLIENT.get(port, host, uri)
                .setQueryParam(queryParamName, queryParamValue)
                .as(bodyCodec)
                .send();
    }

    /**
     * Performs the HTTP Post method request using parameters.
     * @param port port of the server to send the request
     * @param host host address of the server to send the request
     * @param uri complete uri of the API to send the request
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> postJson(final int port,
                                                       final String host,
                                                       final String uri,
                                                       final BodyCodec<T> bodyCodec) {
        return VertxHelper.WEB_CLIENT.post(port, host, uri).as(bodyCodec).send();
    }

    /**
     * Performs the HTTP Post method request using parameters.
     * @param port port of the server to send the request
     * @param host host address of the server to send the request
     * @param uri complete uri of the API to send the request
     * @param json {@link JsonObject} to send as body
     * @param bodyCodec {@link BodyCodec} to parse response body
     * @param <T> type parameter needed to generalize body codec
     * @return the {@link Future} containing the result
     */
    public static <T> Future<HttpResponse<T>> postJson(final int port,
                                                       final String host,
                                                       final String uri,
                                                       final @NotNull JsonObject json,
                                                       final BodyCodec<T> bodyCodec) {
        return VertxHelper.WEB_CLIENT.post(port, host, uri)
                .as(bodyCodec)
                .putHeader("Content-Type", "application/json")
                .sendBuffer(json.toBuffer());
    }

}
