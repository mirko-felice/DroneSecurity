/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttTopicConstants;
import io.github.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceActionForm;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import io.github.dronesecurity.userapplication.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import io.vertx.core.Future;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * Service providing methods both for the needs of the courier and for the maintainer.
 */
public class NegligenceReportService implements CourierNegligenceReportService, MaintainerNegligenceReportService {

    private static NegligenceReportService singleton;

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static NegligenceReportService getInstance() {
        synchronized (NegligenceReportService.class) {
            if (singleton == null)
                singleton = new NegligenceReportService();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToNegligenceReports(final @NotNull Consumer<NewNegligence> consumer) {
        DomainEvents.register(NewNegligence.class, this::onNewNegligence);
        DomainEvents.register(NewNegligence.class, consumer);
        Connection.getInstance().subscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC, msg -> {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode json = mapper.readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                AuthenticationRepository.getInstance().retrieveCourier(json.get(NegligenceConstants.NEGLIGENT).asText())
                        .onSuccess(courier -> {
                            try {
                                final NegligenceReport report = mapper.reader()
                                        .forType(NegligenceReport.class)
                                        .withAttribute(NegligenceConstants.ASSIGNEE, courier.getSupervisor())
                                        .readValue(json.toString());
                                DomainEvents.raise(new NewNegligence(report));
                            } catch (JsonProcessingException e) {
                                LoggerFactory.getLogger(this.getClass()).error("Can NOT convert negligence report.", e);
                            }
                        });
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).error("Can NOT convert negligence report.", e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> takeAction(final NegligenceActionForm form) {
        return NegligenceRepository.getInstance().takeAction(form);
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        NegligenceRepository.getInstance().createReport(newNegligence.getReport());
    }
}
