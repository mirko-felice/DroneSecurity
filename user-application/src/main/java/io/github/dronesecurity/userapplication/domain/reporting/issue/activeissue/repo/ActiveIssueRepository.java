/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;

import java.util.List;

/**
 * Repository that manages the storing of {@link AbstractActiveIssue}.
 */
public interface ActiveIssueRepository {

    /**
     * Gets all active issues of courier.
     * @param username username to retrieve issues from
     * @return the list of active issues contained in repository
     */
    List<AbstractActiveIssue> retrieveActiveIssuesForCourier(String username);

    /**
     * Gets all active issues of assignee.
     * @param username username to retrieve issues from
     * @return the list of active issues contained in repository
     */
    List<AbstractActiveIssue> retrieveActiveIssuesForAssignee(String username);
}
