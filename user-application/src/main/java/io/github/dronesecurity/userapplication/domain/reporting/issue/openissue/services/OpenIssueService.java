/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.services;

import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;

/**
 * Interface representing actions performable on an {@link OpenIssue}.
 */
public interface OpenIssueService {

    /**
     * Informs the database that an {@link OpenIssue} is now visioned by a maintainer.
     * @param issue the issue that is visioned
     * @return whether the update operation has succeeded or not
     */
    boolean visionIssue(VisionedIssue issue);
}
