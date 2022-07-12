/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of non-persistent {@link NegligenceRepository} useful to test business logic.
 */
public class InMemoryNegligenceRepository implements NegligenceRepository {

    private final List<NegligenceReport> reports = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceIdentifier nextNegligenceIdentifier() {
        return NegligenceIdentifier.fromLong(this.reports.size() + 1L);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReport(final OpenNegligenceReport report) {
        this.reports.add(report);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takenAction(final ClosedNegligenceReport report) {
        this.reports.replaceAll(r -> r.hasSameIdentityAs(report) ? report : r);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForNegligent(final Negligent negligent) {
        return this.reports.stream()
                .filter(r -> r.getNegligent().isSameValueAs(negligent))
                .map(OpenNegligenceReport.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForNegligent(final Negligent negligent) {
        return this.reports.stream()
                .filter(r -> r.getNegligent().isSameValueAs(negligent))
                .map(ClosedNegligenceReport.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForAssignee(final Assignee assignee) {
        return this.reports.stream()
                .filter(r -> r.assignedTo().isSameValueAs(assignee))
                .map(OpenNegligenceReport.class::cast)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForAssignee(final Assignee assignee) {
        return this.reports.stream()
                .filter(r -> r.assignedTo().isSameValueAs(assignee))
                .map(ClosedNegligenceReport.class::cast)
                .collect(Collectors.toList());
    }
}
