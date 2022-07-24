/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.repo;

import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;

/**
 * Repository that allows to vision an {@link OpenIssue}.
 */
public interface OpenIssueRepository {

    /**
     * Updates the repository by transforming an {@link OpenIssue} into a {@link VisionedIssue}.
     * @param issue the transformed issue to update the repository with
     * @return whether the operation went successfully or not
     */
    boolean visionIssue(VisionedIssue issue);
}
