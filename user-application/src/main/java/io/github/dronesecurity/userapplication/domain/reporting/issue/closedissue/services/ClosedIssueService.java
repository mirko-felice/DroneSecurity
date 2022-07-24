/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.services;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;

import java.util.List;

/**
 * Interface representing actions performable on a {@link ClosedIssue}.
 */
public interface ClosedIssueService {

    /**
     * Lists issues reported that have been closed.
     * @return the future of the list of closed issues
     */
    List<ClosedIssue> retrieveClosedIssues();
}
