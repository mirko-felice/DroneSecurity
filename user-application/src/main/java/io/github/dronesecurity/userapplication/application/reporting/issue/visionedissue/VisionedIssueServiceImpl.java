/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue.visionedissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.repo.VisionedIssueRepository;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.services.VisionedIssueService;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.visionedissue.VisionedIssueRepositoryImpl;

/**
 * Implementation of the {@link VisionedIssueService}.
 */
public class VisionedIssueServiceImpl implements VisionedIssueService {

    private final VisionedIssueRepository repository;

    /**
     * Instantiates the repository used by this service.
     */
    public VisionedIssueServiceImpl() {
        this.repository = new VisionedIssueRepositoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeIssue(final ClosedIssue issue) {
        return this.repository.closeIssue(issue);
    }
}
