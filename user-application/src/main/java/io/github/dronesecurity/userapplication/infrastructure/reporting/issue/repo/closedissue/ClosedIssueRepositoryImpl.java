/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.closedissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.repo.ClosedIssueRepository;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.IssueRetrievalHelper;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ClosedIssueRepository}.
 */
public class ClosedIssueRepositoryImpl extends MongoRepository implements ClosedIssueRepository {

    private static final String COLLECTION_NAME = "issueReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedIssue> retrieveClosedIssuesForCourier(final String username) {
        final JsonObject closedIssuesQuery = new JsonObject().put(IssueStringHelper.COURIER, username);

        return IssueRetrievalHelper.executeSync(() -> this.getClosedIssuesFromQuery(closedIssuesQuery));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedIssue> retrieveClosedIssuesForAssignee(final String username) {
        final JsonObject closedIssuesQuery = new JsonObject().put(IssueStringHelper.ASSIGNEE, username);

        return IssueRetrievalHelper.executeSync(() -> this.getClosedIssuesFromQuery(closedIssuesQuery));
    }

    private @Nullable List<ClosedIssue> getClosedIssuesFromQuery(final @NotNull JsonObject query) {
        query.put(IssueStringHelper.STATUS, IssueStringHelper.STATUS_CLOSED);

        return this.waitFutureResult(this.mongo().find(COLLECTION_NAME, query)
                .transform(issues -> {
                    List<ClosedIssue> result = Collections.emptyList();
                    if (!issues.result().isEmpty())
                        result = issues.result().stream()
                                .map(json -> Json.decodeValue(json.toString(), ClosedIssue.class))
                                .collect(Collectors.toList());

                    return Future.succeededFuture(result);
                }));
    }
}
