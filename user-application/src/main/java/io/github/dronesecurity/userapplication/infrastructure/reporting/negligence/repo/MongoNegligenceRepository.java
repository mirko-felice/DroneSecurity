/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link NegligenceRepository} to work with the underlying DB.
 */
public final class MongoNegligenceRepository extends MongoRepository implements NegligenceRepository {

    private static final String COLLECTION_NAME = "negligenceReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceIdentifier nextNegligenceIdentifier() {
        return this.waitFutureResult(this.mongo().count(COLLECTION_NAME, null)
                .map(value -> value == 0 ? NegligenceIdentifier.first() : NegligenceIdentifier.fromLong(value)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReport(final OpenNegligenceReport report) {
        this.waitFutureResult(this.mongo().save(COLLECTION_NAME, JsonObject.mapFrom(report)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takenAction(final @NotNull ClosedNegligenceReport report) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ID, report.getId().asLong());
        this.waitFutureResult(this.mongo().findOneAndReplace(COLLECTION_NAME, query, JsonObject.mapFrom(report)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForNegligent(final @NotNull Negligent negligent) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, negligent.asString());
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForNegligent(final @NotNull Negligent negligent) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, negligent.asString());
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForAssignee(final @NotNull Assignee assignee) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, assignee.asString());
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForAssignee(final @NotNull Assignee assignee) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, assignee.asString());
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    private @Nullable <T extends NegligenceReport> List<T> retrieveReportsForUser(final JsonObject query,
                                                                                  final Class<T> clazz) {
        return this.waitFutureResult(this.mongo().find(COLLECTION_NAME, query)
                .map(reports -> reports.stream()
                        .map(report -> Json.decodeValue(report.toString(), NegligenceReport.class))
                        .filter(clazz::isInstance)
                        .map(clazz::cast)
                        .collect(Collectors.toList())));
    }
}
