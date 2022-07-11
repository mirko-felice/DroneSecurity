/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.negligence;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.events.NegligenceReportClosed;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.AssigneeReportsManager;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation of {@link AssigneeReportsManager}.
 */
public final class AssigneeReportsManagerImpl implements AssigneeReportsManager {

    private final NegligenceRepository negligenceRepository;
    private final Consumer<NegligenceReportClosed> negligenceReportClosedHandler;

    /**
     * Build the assignee reports' manager.
     * @param negligenceRepository {@link NegligenceRepository} to retrieve entities
     */
    public AssigneeReportsManagerImpl(final NegligenceRepository negligenceRepository) {
        this.negligenceRepository = negligenceRepository;
        this.negligenceReportClosedHandler = this::onNegligenceReportClosed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeAction(final @NotNull OpenNegligenceReport report, final NegligenceActionForm actionForm) {
        DomainEvents.register(NegligenceReportClosed.class, this.negligenceReportClosedHandler);
        report.takeAction(actionForm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForAssignee(final Assignee assignee) {
        return this.negligenceRepository.retrieveOpenReportsForAssignee(assignee);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForAssignee(final Assignee assignee) {
        return this.negligenceRepository.retrieveClosedReportsForAssignee(assignee);
    }

    private void onNegligenceReportClosed(final @NotNull NegligenceReportClosed negligenceReportClosed) {
        this.negligenceRepository.takenAction(negligenceReportClosed.getReport());
        DomainEvents.unregister(NegligenceReportClosed.class, this.negligenceReportClosedHandler);
    }
}
