/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceSolution;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.utilities.NegligenceConstants;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

/**
 * Service providing methods both for the needs of the courier and for the maintainer.
 */
public final class NegligenceReportService
        implements CourierNegligenceReportService, MaintainerNegligenceReportService {

    private static final String ERROR_MESSAGE = "Can NOT convert negligence report.";
    private static final NegligenceRepository REPOSITORY = NegligenceRepository.getInstance();
    private final Consumer<NewNegligence> newNegligenceHandler;

    /**
     * Build the service.
     */
    public NegligenceReportService() {
        this.newNegligenceHandler = this::onNewNegligence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToNewNegligence() {
        DomainEvents.register(NewNegligence.class, this.newNegligenceHandler);
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT).onSuccess(res -> {
            final GenericUser user = Json.decodeValue(res.bodyAsJsonObject().toBuffer(), GenericUser.class);
            this.subscribeToUserNegligence(user.getUsername()); // TODO pass username as function parameter
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsubscribeFromNewNegligence() {
        DomainEvents.unregister(NewNegligence.class, this.newNegligenceHandler);
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT).onSuccess(res -> {
            final GenericUser user = Json.decodeValue(res.bodyAsJsonObject().toBuffer(), GenericUser.class);
            this.subscribeToUserNegligence(user.getUsername()); // TODO pass username as function parameter
            Connection.getInstance().unsubscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC + user.getUsername());
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToCourierNegligence(final String courier) {
        this.subscribeToUserNegligence(courier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(final String username) {
        return REPOSITORY.retrieveOpenReportsForCourier(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(final String username) {
        return REPOSITORY.retrieveClosedReportsForCourier(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(final String username) {
        return REPOSITORY.retrieveOpenReportsForMaintainer(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(final String username) {
        return REPOSITORY.retrieveClosedReportsForMaintainer(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> takeAction(final OpenNegligenceReport report, final NegligenceSolution solution) {
        return REPOSITORY.takeAction(report, solution);
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        REPOSITORY.createReport(newNegligence.getReport());
    }

    private void subscribeToUserNegligence(final String username) {
        Connection.getInstance().subscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC + username, msg -> {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode json = mapper.readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final JsonObject body =
                        Json.encodeToBuffer(json.get(NegligenceConstants.NEGLIGENT).asText()).toJsonObject();
                UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_COURIER_BY_USERNAME, body).onSuccess(res -> {
                    final Courier courier = Json.decodeValue(res.bodyAsJsonObject().toBuffer(), Courier.class);
                    final NegligenceReport report;
                    try {
                        report = mapper.reader()
                                .forType(NegligenceReport.class)
                                .withAttribute(NegligenceConstants.ASSIGNEE, courier.getSupervisorUsername())
                                .readValue(json.toString());
                        DomainEvents.raise(new NewNegligence(report));
                    } catch (JsonProcessingException e) {
                        LoggerFactory.getLogger(this.getClass()).error(ERROR_MESSAGE, e);
                    }
                });
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).error(ERROR_MESSAGE, e);
            }
        });
    }
}
