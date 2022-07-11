/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.negligence;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.impl.OpenNegligenceReportImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.events.NewNegligence;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.DroneReporter;
import io.github.dronesecurity.userapplication.events.DomainEvents;

/**
 * Implementation of {@link DroneReporter}.
 */
public final class DroneReporterImpl implements DroneReporter {

    private final NegligenceRepository negligenceRepository;

    /**
     * Build the drone reporter.
     * @param negligenceRepository {@link NegligenceRepository} to retrieve entities
     */
    public DroneReporterImpl(final NegligenceRepository negligenceRepository) {
        this.negligenceRepository = negligenceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reportsNegligence(final Negligent negligent, final Assignee assignee, final DroneData data) {
        final OpenNegligenceReport report = new OpenNegligenceReportImpl(
                this.negligenceRepository.nextNegligenceIdentifier(),
                negligent,
                assignee,
                data);
        this.negligenceRepository.createReport(report);
        DomainEvents.raise(new NewNegligence(report));
    }
}
