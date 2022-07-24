/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.openissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.objects.IssueId;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.repo.OpenIssueRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implementation of an {@link OpenIssueRepository}.
 */
public class OpenIssueRepositoryImpl extends MongoRepository implements OpenIssueRepository {

    private static final String COLLECTION_NAME = "issueReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visionIssue(final @NotNull VisionedIssue issue) {
        final JsonObject newStatus = new JsonObject()
                .put(IssueStringHelper.STATUS, issue.getState());
        return this.updateIssue(issue.getId(), newStatus);
    }

    private boolean updateIssue(final @NotNull IssueId issueId, final JsonObject updatingValues) {
        final JsonObject query = new JsonObject();
        query.put(IssueStringHelper.ID, issueId.getIssueId());
        final JsonObject update = new JsonObject().put("$set", updatingValues);

        return Boolean.TRUE.equals(
                this.waitFutureResult(
                        this.mongo().findOneAndUpdate(COLLECTION_NAME, query, update).map(Objects::nonNull)));
    }
}
