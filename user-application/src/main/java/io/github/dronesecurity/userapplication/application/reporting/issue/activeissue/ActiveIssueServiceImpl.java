/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue.activeissue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.repo.ActiveIssueRepository;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.services.ActiveIssueService;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.repo.activeissue.ActiveIssueRepositoryImpl;

import java.util.List;

/**
 * Implementation of {@link ActiveIssueService}.
 */
public class ActiveIssueServiceImpl implements ActiveIssueService {

    private final ActiveIssueRepository repository;

    /**
     * Instantiates the repository for this service.
     */
    public ActiveIssueServiceImpl() {
        this.repository = new ActiveIssueRepositoryImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractActiveIssue> retrieveActiveIssuesForCourier(final String username) {
        return this.repository.retrieveActiveIssuesForCourier(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractActiveIssue> retrieveActiveIssuesForAssignee(final String username) {
        return this.repository.retrieveActiveIssuesForAssignee(username);
    }
}
