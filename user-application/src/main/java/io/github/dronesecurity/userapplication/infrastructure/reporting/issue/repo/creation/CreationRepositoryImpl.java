/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.creation;

import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.repo.CreationRepository;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;

/**
 * Implementation of a {@link CreationRepository}.
 */
public class CreationRepositoryImpl extends MongoRepository implements CreationRepository {

    private static final String MONGO_ID = "_id";
    private static final String COLLECTION_NAME = "issueReports";

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonObject addIssue(final SendingIssue issue) {
        final JsonObject newIssue = new JsonObject();
        return this.waitFutureResult(this.getLastID().transform(id -> {
            if (id.succeeded())
                newIssue.put(IssueStringHelper.ID, id.result() + 1);
            else
                newIssue.put(IssueStringHelper.ID, 1);
            newIssue.mergeIn(JsonObject.mapFrom(issue), true);
            return this.mongo().save(COLLECTION_NAME, newIssue)
                    .map(mongoId -> newIssue.put(MONGO_ID, mongoId));
        }));
    }

    private Future<Long> getLastID() {
        final FindOptions options = new FindOptions();
        options.setSort(new JsonObject().put(IssueStringHelper.ID, -1));
        options.setLimit(1);
        return this.mongo().findWithOptions(COLLECTION_NAME, new JsonObject(), options)
                .map(val -> val.get(0).getLong(IssueStringHelper.ID));
    }
}
