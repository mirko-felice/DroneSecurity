/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue.closedissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.repo.ClosedIssueRepository;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.services.ClosedIssueService;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.closedissue.ClosedIssueRepositoryImpl;

import java.util.List;

/**
 * Implementation of {@link ClosedIssueService}.
 */
public class ClosedIssueServiceImpl implements ClosedIssueService {

    private final ClosedIssueRepository repository;

    /**
     * Instantiates the repository for this service.
     */
    public ClosedIssueServiceImpl() {
        this.repository = new ClosedIssueRepositoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedIssue> retrieveClosedIssues() {
        return this.repository.retrieveClosedIssues();
    }
}
