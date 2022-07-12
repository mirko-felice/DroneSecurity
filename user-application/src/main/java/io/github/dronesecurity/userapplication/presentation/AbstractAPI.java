/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generic class representing every API as an {@link AbstractVerticle}.
 */
public abstract class AbstractAPI extends AbstractVerticle {

    private static final String SEP = "/";
    private static final String DEFAULT_KEY = "default";

    /**
     * Base API url valid for all API.
     */
    protected static final String BASE_API_URL = "https://raw.githubusercontent.com/mirko-felice/DroneSecurity/"
            + "hotfix/application-issues/user-application/src/main/resources/io/github/dronesecurity/userapplication/"
            + "presentation/";

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final @NotNull Promise<Void> startPromise) {
        final Router globalRouter = Router.router(this.getVertx());
        RouterBuilder.create(this.getVertx(), this.getOpenAPISpecUrl())
                .onSuccess(routerBuilder -> {
                    this.setupOperations(routerBuilder);

                    final JsonArray servers = routerBuilder.getOpenAPI().getOpenAPI().getJsonArray("servers");
                    final int serversCount = servers.size();
                    final List<Future<?>> futures = new ArrayList<>(serversCount);
                    for (int i = 0; i < serversCount; i++) {
                        final JsonObject server = servers.getJsonObject(i);
                        final JsonObject variables = server.getJsonObject("variables");

                        final String basePath = SEP + variables.getJsonObject("basePath").getString(DEFAULT_KEY)
                                + SEP + "*";
                        final int port = Integer.parseInt(variables.getJsonObject("port").getString(DEFAULT_KEY));
                        final String host = variables.getJsonObject("host").getString(DEFAULT_KEY);

                        globalRouter.route(basePath).subRouter(routerBuilder.createRouter());
                        futures.add(this.getVertx().createHttpServer().requestHandler(globalRouter).listen(port, host));
                    }
                    CompositeFuture.all(Arrays.asList(futures.toArray(new Future[0])))
                            .onSuccess(ignored -> startPromise.complete());
                })
                .onFailure(startPromise::fail);
    }

    /**
     * Retrieves Open API Specification url.
     * @return url of the Open API Specification
     */
    protected abstract String getOpenAPISpecUrl();

    /**
     * Set up the operations available to this API.
     * @param routerBuilder {@link RouterBuilder} to set operations on
     */
    protected abstract void setupOperations(RouterBuilder routerBuilder);

}
