/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities.shipping;

import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
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
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> getHTTP(final int port, final String host, final String uri) {
        return VertxHelper.WEB_CLIENT.get(port, host, uri).send();
    }

    /**
     * Performs the HTTP Post method request using parameters.
     * @param port port of the server to send the request
     * @param host host address of the server to send the request
     * @param uri complete uri of the API to send the request
     * @param json {@link JsonObject} to send as body
     * @return the {@link Future} containing the result
     */
    public static Future<HttpResponse<Buffer>> postJson(final int port, final String host, final String uri,
                                                        final @NotNull JsonObject json) {
        return VertxHelper.WEB_CLIENT.post(port, host, uri)
                .putHeader("Content-Type", "application/json")
                .sendBuffer(json.toBuffer());
    }

}
