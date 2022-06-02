/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.issue.services;

import io.github.dronesecurity.userapplication.reporting.issue.entities.*;
import io.github.dronesecurity.userapplication.reporting.issue.repo.IssueReportRepository;
import io.vertx.core.Future;

import java.util.List;

/**
 * The service for issue reporting.
 */
public final class IssueReportService implements CourierIssueReportService, MaintainerIssueReportService {

    private static final IssueReportRepository REPOSITORY = IssueReportRepository.getInstance();
    private static IssueReportService singleton;

    private IssueReportService() { }

    /**
     * Gets the singleton instance.
     * @return the singleton
     */
    public static IssueReportService getInstance() {
        synchronized (IssueReportService.class) {
            if (singleton == null)
                singleton = new IssueReportService();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<CreatedIssue>> getOpenIssueReports() {
        return REPOSITORY.getOpenIssues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<List<ClosedIssue>> getClosedIssueReports() {
        return REPOSITORY.getClosedIssues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> addIssueReport(final Issue issue) {
        return REPOSITORY.addIssue(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> visionIssue(final OpenIssue issue) {
        return REPOSITORY.visionOpenIssue(issue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> closeIssue(final VisionedIssue issue, final String solution) {
        return REPOSITORY.closeVisionedIssue(issue, solution);
    }
}
