/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue.openissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.repo.OpenIssueRepository;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.services.OpenIssueService;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.openissue.OpenIssueRepositoryImpl;

/**
 * Implementation of the {@link OpenIssueService}.
 */
public class OpenIssueServiceImpl implements OpenIssueService {

    private final OpenIssueRepository repository;

    /**
     * Instantiates the repository used by this service.
     */
    public OpenIssueServiceImpl() {
        this.repository = new OpenIssueRepositoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visionIssue(final VisionedIssue issue) {
        return this.repository.visionIssue(issue);
    }
}
