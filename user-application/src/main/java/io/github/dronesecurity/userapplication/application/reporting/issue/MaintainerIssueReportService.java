/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.issue;

import io.github.dronesecurity.userapplication.application.reporting.issue.openissue.OpenIssueServiceImpl;
import io.github.dronesecurity.userapplication.application.reporting.issue.visionedissue.VisionedIssueServiceImpl;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.creation.entities.SendingIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.services.OpenIssueService;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.services.VisionedIssueService;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;

/**
 * Issue report service to be used when {@link Maintainer} is the logged user.
 */
public class MaintainerIssueReportService extends AbstractIssueReportRetrievalService {

    private final OpenIssueService openIssueService;
    private final VisionedIssueService visionedIssueService;

    /**
     * Instantiates all services used by the {@link Maintainer}.
     */
    public MaintainerIssueReportService() {
        this.openIssueService = new OpenIssueServiceImpl();
        this.visionedIssueService = new VisionedIssueServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIssueReport(final SendingIssue issue) {
        // Maintainer does create a new issue.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visionIssue(final VisionedIssue issue) {
        return this.openIssueService.visionIssue(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeIssue(final ClosedIssue issue) {
        return this.visionedIssueService.closeIssue(issue);
    }
}
