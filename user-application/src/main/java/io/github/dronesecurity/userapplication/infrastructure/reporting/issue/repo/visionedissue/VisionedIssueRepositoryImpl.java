/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.visionedissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.objects.IssueId;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.repo.VisionedIssueRepository;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Implementation of a {@link VisionedIssueRepository}.
 */
public class VisionedIssueRepositoryImpl extends MongoRepository implements VisionedIssueRepository {

    private static final String COLLECTION_NAME = "issueReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeIssue(final @NotNull ClosedIssue issue) {
        final JsonObject update = new JsonObject()
                .put(IssueStringHelper.STATUS, issue.getState())
                .put(IssueStringHelper.SOLUTION, issue.getIssueSolution());

        return this.updateIssue(issue.getId(), update);
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
