/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.CreatedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.Issue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository operating on Issue Reports regarding drones sent by the Courier.
 */
public interface IssueReportRepository {

    /**
     * Add new open issue.
     * @param issue the issue to add to repository
     * @return the {@link Future} containing the insertion object
     */
    Future<JsonObject> addIssue(Issue issue);

    /**
     * Updates a {@link CreatedIssue} and sets its state with the new one.
     * @param issue the issue with the new state to be updated
     * @return whether the operation went successfully or not
     */
    Future<Boolean> updateIssueState(CreatedIssue issue);

    /**
     * Closes an existing issue.
     * @param issue the issue to be closed
     * @return whether the operation went successfully or not
     */
    Future<Boolean> closeIssue(ClosedIssue issue);

    /**
     * Gets all open issues of the currently logged user.
     * @return the list of open issues contained in repository
     */
    Future<List<CreatedIssue>> getOpenIssues();

    /**
     * Gets all closed issues of the currently logged user.
     * @return the list of closed issues contained in repository
     */
    Future<List<ClosedIssue>> getClosedIssues();

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull IssueReportRepository getInstance() {
        return IssueReportRepositoryImpl.getInstance();
    }
}
