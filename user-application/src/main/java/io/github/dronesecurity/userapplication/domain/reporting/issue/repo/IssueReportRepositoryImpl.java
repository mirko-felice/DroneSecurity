/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.repo;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.UserRole;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.CreatedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.Issue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.serialization.IssueStringHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link IssueReportRepository}.
 */
public final class IssueReportRepositoryImpl implements IssueReportRepository {

    private static final String MONGO_ID = "_id";
    private static final String COLLECTION_NAME = "issueReports";
    private static IssueReportRepository singleton;

    private IssueReportRepositoryImpl() { }

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static IssueReportRepository getInstance() {
        synchronized (IssueReportRepositoryImpl.class) {
            if (singleton == null)
                singleton = new IssueReportRepositoryImpl();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<JsonObject> addIssue(final Issue issue) {
        final JsonObject newIssue = new JsonObject();
        return this.getLastID().transform(id -> {
            if (id.succeeded())
                newIssue.put(IssueStringHelper.ID, id.result() + 1);
            else
                newIssue.put(IssueStringHelper.ID, 1);
            newIssue.mergeIn(JsonObject.mapFrom(issue), true);
            return VertxHelper.MONGO_CLIENT.save(COLLECTION_NAME, newIssue)
                    .map(mongoId -> newIssue.put(MONGO_ID, mongoId));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> updateIssueState(final @NotNull CreatedIssue issue) {
        final JsonObject newStatus = new JsonObject()
                .put(IssueStringHelper.STATUS, issue.getState());
        return this.updateIssue(issue.getId(), newStatus);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> closeIssue(final @NotNull ClosedIssue issue) {
        final JsonObject update = new JsonObject()
                .put(IssueStringHelper.STATUS, issue.getState())
                .put(IssueStringHelper.SOLUTION, issue.getIssueSolution());

        return this.updateIssue(issue.getId(), update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<CreatedIssue>> getOpenIssues() {
        final JsonObject openIssuesQuery = this.initQueryWithUserData();

        return this.getOpenIssueFromQuery(openIssuesQuery);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<ClosedIssue>> getClosedIssues() {
        final JsonObject closedIssuesQuery = this.initQueryWithUserData();

        return this.getClosedIssuesFromQuery(closedIssuesQuery);
    }

    private Future<Long> getLastID() {
        final FindOptions options = new FindOptions();
        options.setSort(new JsonObject().put(IssueStringHelper.ID, -1));
        options.setLimit(1);
        return VertxHelper.MONGO_CLIENT.findWithOptions(COLLECTION_NAME, new JsonObject(), options)
                .map(val -> val.get(0).getLong(IssueStringHelper.ID));
    }

    private @NotNull JsonObject initQueryWithUserData() {
        final JsonObject queryWithUser = new JsonObject();
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE).onSuccess(res -> {
            final GenericUser[] loggedUser = new GenericUser[1]; // TODO
            switch (UserRole.valueOf(res.bodyAsString())) {
                case COURIER:
                    UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT)
                            .onSuccess(response -> loggedUser[0] =
                                    Json.decodeValue(response.bodyAsJsonObject().toBuffer(), GenericUser.class));
                    break;
                case MAINTAINER:
                    UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT)
                            .onSuccess(response -> loggedUser[0] =
                                    Json.decodeValue(response.bodyAsJsonObject().toBuffer(), GenericUser.class));
                    break;
                case NOT_LOGGED:
                default:
                    // TODO
            }
            if (loggedUser[0].getRole() == UserRole.COURIER)
                queryWithUser.put(IssueStringHelper.COURIER, loggedUser[0].getUsername());
            else if (loggedUser[0].getRole() == UserRole.MAINTAINER)
                queryWithUser.put(IssueStringHelper.ASSIGNEE, loggedUser[0].getUsername());
        });

        return queryWithUser;
    }

    private Future<List<CreatedIssue>> getOpenIssueFromQuery(final @NotNull JsonObject query) {
        final JsonObject statusOpenValues = new JsonObject();
        statusOpenValues.put("$in", new JsonArray().add(IssueStringHelper.STATUS_OPEN)
                .add(IssueStringHelper.STATUS_VISIONED));
        query.put(IssueStringHelper.STATUS, statusOpenValues);

        return VertxHelper.MONGO_CLIENT.find(COLLECTION_NAME, query)
                .transform(issues -> {
                    List<CreatedIssue> result = Collections.emptyList();
                    if (!issues.result().isEmpty())
                        result = issues.result().stream()
                                .map(json -> Json.decodeValue(json.toString(), CreatedIssue.class))
                                .collect(Collectors.toList());

                    return Future.succeededFuture(result);
                });
    }

    private Future<List<ClosedIssue>> getClosedIssuesFromQuery(final @NotNull JsonObject query) {
        query.put(IssueStringHelper.STATUS, IssueStringHelper.STATUS_CLOSED);

        return VertxHelper.MONGO_CLIENT.find(COLLECTION_NAME, query)
                .transform(issues -> {
                    List<ClosedIssue> result = Collections.emptyList();
                    if (!issues.result().isEmpty())
                        result = issues.result().stream()
                                .map(json -> Json.decodeValue(json.toString(), ClosedIssue.class))
                                .collect(Collectors.toList());

                    return Future.succeededFuture(result);
                });
    }

    private Future<Boolean> updateIssue(final int issueId, final JsonObject updatingValues) {
        final JsonObject query = new JsonObject();
        query.put(IssueStringHelper.ID, issueId);
        final JsonObject update = new JsonObject().put("$set", updatingValues);

        return VertxHelper.MONGO_CLIENT.findOneAndUpdate(COLLECTION_NAME, query, update).map(Objects::nonNull);
    }
}
