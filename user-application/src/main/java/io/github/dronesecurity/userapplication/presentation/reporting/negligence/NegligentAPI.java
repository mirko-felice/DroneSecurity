/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.application.reporting.negligence.DroneReporterSubscriber;
import io.github.dronesecurity.userapplication.application.reporting.negligence.NegligentReportsManagerImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.NegligentReportsManager;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo.MongoNegligenceRepository;
import io.github.dronesecurity.userapplication.presentation.AbstractAPI;
import io.github.dronesecurity.userapplication.utilities.reporting.negligence.NegligentAPIHelper;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

/**
 * API exposing operations related to the negligent.
 */
public final class NegligentAPI extends AbstractAPI {

    private static final String OPEN_API_URL = AbstractAPI.BASE_API_URL + "reporting/negligence/negligentAPI.json";
    private final NegligentReportsManager negligentReportsManager;

    /**
     * Build the API.
     */
    public NegligentAPI() {
        this.negligentReportsManager = new NegligentReportsManagerImpl(new MongoNegligenceRepository());
        DroneReporterSubscriber.startReceivingNegligentReports();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(final Promise<Void> stopPromise) throws Exception {
        DroneReporterSubscriber.stopReceivingNegligentReports();
        super.stop(stopPromise);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOpenAPISpecUrl() {
        return OPEN_API_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setupOperations(final @NotNull RouterBuilder routerBuilder) {
        routerBuilder.operation(NegligentAPIHelper.Operation.RETRIEVE_OPEN_REPORTS.toString())
                .handler(this::retrieveOpenReportsForNegligent);
        routerBuilder.operation(NegligentAPIHelper.Operation.RETRIEVE_CLOSED_REPORTS.toString())
                .handler(this::retrieveClosedReportsForNegligent);
    }

    private void retrieveOpenReportsForNegligent(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final Negligent negligent = Negligent.parse(body.getString(NegligentAPIHelper.NEGLIGENT_KEY));
        routingContext.response().end(Json.encodePrettily(
                this.executeSync(() -> this.negligentReportsManager.retrieveOpenReportsForNegligent(negligent))));
    }

    private void retrieveClosedReportsForNegligent(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final Negligent negligent = Negligent.parse(body.getString(NegligentAPIHelper.NEGLIGENT_KEY));
        routingContext.response().end(Json.encodePrettily(
                this.executeSync(() -> this.negligentReportsManager.retrieveClosedReportsForNegligent(negligent))));
    }
}
