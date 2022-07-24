/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.activeissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.repo.ActiveIssueRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.IssueRetrievalHelper;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of an {@link ActiveIssueRepository}.
 */
public class ActiveIssueRepositoryImpl extends MongoRepository implements ActiveIssueRepository {

    private static final String COLLECTION_NAME = "issueReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractActiveIssue> retrieveActiveIssues() {
        final JsonObject openIssuesQuery = IssueRetrievalHelper.initQueryWithUserData();

        return this.getActiveIssuesFromQuery(openIssuesQuery);
    }

    private @Nullable List<AbstractActiveIssue> getActiveIssuesFromQuery(final @NotNull JsonObject query) {
        final JsonObject statusOpenValues = new JsonObject();
        statusOpenValues.put("$in", new JsonArray().add(IssueStringHelper.STATUS_OPEN)
                .add(IssueStringHelper.STATUS_VISIONED));
        query.put(IssueStringHelper.STATUS, statusOpenValues);

        return this.waitFutureResult(this.mongo().find(COLLECTION_NAME, query)
                .transform(issues -> {
                    List<AbstractActiveIssue> result = Collections.emptyList();
                    if (!issues.result().isEmpty())
                        result = issues.result().stream()
                                .map(json -> Json.decodeValue(json.toString(), AbstractActiveIssue.class))
                                .collect(Collectors.toList());

                    return Future.succeededFuture(result);
                }));
    }
}
