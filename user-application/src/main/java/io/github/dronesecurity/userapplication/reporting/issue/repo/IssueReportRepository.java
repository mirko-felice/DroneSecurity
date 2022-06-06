/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.repo;

import io.github.dronesecurity.userapplication.reporting.issue.entities.*;
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
     * Updates an existing {@link OpenIssue} and sets it as visioned.
     * @param issue the issue to be updated
     * @return whether the operation went successfully or not
     */
    Future<Boolean> visionOpenIssue(OpenIssue issue);

    /**
     * Updates an existing {@link VisionedIssue} and sets it as closed.
     * @param issue the issue to be updated
     * @param solution string representing the solution used by the maintainer
     * @return whether the operation went successfully or not
     */
    Future<Boolean> closeVisionedIssue(VisionedIssue issue, String solution);

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
