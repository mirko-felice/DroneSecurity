/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.*;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.utilities.NegligenceConstants;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link NegligenceRepository}.
 */
public final class NegligenceRepositoryImpl implements NegligenceRepository {

    private static final String COLLECTION_NAME = "negligenceReports";
    private static NegligenceRepositoryImpl singleton;

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static NegligenceRepositoryImpl getInstance() {
        synchronized (NegligenceRepositoryImpl.class) {
            if (singleton == null)
                singleton = new NegligenceRepositoryImpl();
            return singleton;
        }
    }

    @Override
    public void createReport(final NegligenceReport report) {
        final JsonArray command = new JsonArray().add(
                new JsonObject()
                        .put("$group", new JsonObject()
                                .putNull("_id")
                                .put("maxID", new JsonObject()
                                        .put("$max", "$" + NegligenceConstants.ID)
                                )
                        )
        );
        final long[] id = {1};
        VertxHelper.MONGO_CLIENT.aggregate(COLLECTION_NAME, command)
                .handler(result -> id[0] = result.getLong("maxID") + 1)
                .endHandler(ignored -> {
                    final NegligenceReportWithID reportWithID = NegligenceReportFactory.withID(id[0], report);
                    VertxHelper.MONGO_CLIENT.save(COLLECTION_NAME, JsonObject.mapFrom(reportWithID));
                });
    }

    @Override
    public Future<Void> takeAction(final @NotNull OpenNegligenceReport report, final NegligenceSolution solution) {
        final ClosedNegligenceReport closedReport = report.close(Instant.now(), solution);
        final JsonObject query = new JsonObject().put(NegligenceConstants.ID, report.getId());
        return VertxHelper.MONGO_CLIENT.findOneAndReplace(COLLECTION_NAME,
                query,
                JsonObject.mapFrom(closedReport))
                .mapEmpty();
    }

    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, username);
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, username);
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, username);
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, username);
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    private <T extends NegligenceReportWithID> Future<List<T>> retrieveReportsForUser(final JsonObject query,
                                                                                final Class<T> clazz) {
        return VertxHelper.MONGO_CLIENT.find(COLLECTION_NAME, query)
                .map(reports -> reports.stream()
                        .map(report -> Json.decodeValue(report.toString(), NegligenceReportWithID.class))
                        .filter(clazz::isInstance)
                        .map(clazz::cast)
                        .collect(Collectors.toList()));
    }
}
