/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.services;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.Issue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;

/**
 * Interface representing actions performable on a {@link VisionedIssue}.
 */
public interface VisionedIssueService {

    /**
     * Informs the database that an {@link Issue} has been closed by a maintainer
     * and there is a new {@link ClosedIssue} in the system.
     * @param issue the issue that was closed
     * @return whether the update operation has succeeded or not
     */
    boolean closeIssue(ClosedIssue issue);
}
