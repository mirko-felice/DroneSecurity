/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;

import java.util.List;

/**
 * Repository that manages the storing of {@link ClosedIssue}.
 */
public interface ClosedIssueRepository {

    /**
     * Gets all closed issues of the courier.
     * @param username username to retrieve issues from
     * @return the list of closed issues contained in repository
     */
    List<ClosedIssue> retrieveClosedIssuesForCourier(String username);

    /**
     * Gets all closed issues of the assignee.
     * @param username username to retrieve issues from
     * @return the list of closed issues contained in repository
     */
    List<ClosedIssue> retrieveClosedIssuesForAssignee(String username);
}
