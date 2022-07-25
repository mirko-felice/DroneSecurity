/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.services;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;

import java.util.List;

/**
 * Interface representing actions performable on an {@link AbstractActiveIssue}.
 */
public interface ActiveIssueService {

    /**
     * Lists active issues reported for the courier.
     * @param username username to retrieve issues from
     * @return the future of the list of active issues reported
     */
    List<AbstractActiveIssue> retrieveActiveIssuesForCourier(String username);

    /**
     * Lists active issues reported for the assignee.
     * @param username username to retrieve issues from
     * @return the future of the list of active issues reported
     */
    List<AbstractActiveIssue> retrieveActiveIssuesForAssignee(String username);
}
