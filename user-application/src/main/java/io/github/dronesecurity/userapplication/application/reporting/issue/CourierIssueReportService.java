/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue;

import io.github.dronesecurity.userapplication.application.reporting.issue.creation.CreationServiceImpl;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.services.CreationService;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;

/**
 * Issue report service to be used when {@link Courier} is the logged user.
 */
public class CourierIssueReportService extends AbstractIssueReportRetrievalService {

    private final CreationService creationService;

    /**
     * Instantiates all services used by the {@link Courier}.
     */
    public CourierIssueReportService() {
        this.creationService = new CreationServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssueReport(final SendingIssue issue) {
        this.creationService.addIssueReport(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visionIssue(final VisionedIssue issue) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeIssue(final ClosedIssue issue) {
        return false;
    }
}
