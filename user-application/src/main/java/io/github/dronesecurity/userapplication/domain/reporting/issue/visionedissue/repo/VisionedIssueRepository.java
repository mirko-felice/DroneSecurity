/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;

public interface VisionedIssueRepository {

    /**
     * Closes an existing issue.
     * @param issue the issue to be closed
     * @return whether the operation went successfully or not
     */
    boolean closeIssue(ClosedIssue issue);
}
