/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.application.reporting.negligence.AssigneeReportsManagerImpl;
import io.github.dronesecurity.userapplication.application.reporting.negligence.DroneReporterSubscriber;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.AssigneeReportsManager;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo.MongoNegligenceRepository;
import io.github.dronesecurity.userapplication.presentation.AbstractAPI;
import io.github.dronesecurity.userapplication.utilities.reporting.negligence.AssigneeAPIHelper;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.ext.web.validation.ValidationHandler;
import org.jetbrains.annotations.NotNull;

/**
 * API exposing operations related to the assignee.
 */
public final class AssigneeAPI extends AbstractAPI {

    private static final String OPEN_API_URL = AbstractAPI.BASE_API_URL + "reporting/negligence/assigneeAPI.json";
    private final AssigneeReportsManager assigneeReportsManager;

    /**
     * Build the API.
     */
    public AssigneeAPI() {
        this.assigneeReportsManager = new AssigneeReportsManagerImpl(new MongoNegligenceRepository());
        DroneReporterSubscriber.startReceivingAssigneeReports();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop(final Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
        DroneReporterSubscriber.stopReceivingAssigneeReports();
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
        routerBuilder.operation(AssigneeAPIHelper.Operation.RETRIEVE_OPEN_REPORTS.toString())
                .handler(this::retrieveOpenReportsForAssignee);
        routerBuilder.operation(AssigneeAPIHelper.Operation.RETRIEVE_CLOSED_REPORTS.toString())
                .handler(this::retrieveClosedReportsForAssignee);
        routerBuilder.operation(AssigneeAPIHelper.Operation.TAKE_ACTION.toString())
                .handler(this::takeAction);
    }

    private void retrieveOpenReportsForAssignee(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final Assignee assignee = Assignee.parse(body.getString(AssigneeAPIHelper.ASSIGNEE_KEY));
        routingContext.response().end(Json.encodePrettily(
                this.assigneeReportsManager.retrieveOpenReportsForAssignee(assignee)));
    }

    private void retrieveClosedReportsForAssignee(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final Assignee assignee = Assignee.parse(body.getString(AssigneeAPIHelper.ASSIGNEE_KEY));
        routingContext.response().end(Json.encodePrettily(
                this.assigneeReportsManager.retrieveClosedReportsForAssignee(assignee)));
    }

    private void takeAction(final @NotNull RoutingContext routingContext) {
        final RequestParameters params = routingContext.get(ValidationHandler.REQUEST_CONTEXT_KEY);
        final JsonObject body = params.body().getJsonObject();
        final OpenNegligenceReport report =
                Json.decodeValue(body.getBuffer(AssigneeAPIHelper.REPORT_KEY), OpenNegligenceReport.class);
        final NegligenceActionForm negligenceActionForm =
                Json.decodeValue(body.getBuffer(AssigneeAPIHelper.ACTION_FORM_KEY), NegligenceActionForm.class);
        this.assigneeReportsManager.takeAction(report, negligenceActionForm);
        routingContext.response().end();
    }

}
