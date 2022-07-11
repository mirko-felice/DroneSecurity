/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceIdentifier;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;

import java.util.List;

/**
 * Repository to persist changes on {@link NegligenceReport} aggregate.
 */
public interface NegligenceRepository {

    /**
     * Retrieve the next {@link NegligenceIdentifier}.
     * @return the {@link NegligenceIdentifier}
     */
    NegligenceIdentifier nextNegligenceIdentifier();

    /**
     * Saves the report.
     * @param report the {@link OpenNegligenceReport} to save
     */
    void createReport(OpenNegligenceReport report);

    /**
     * Saves the action taken to close the report.
     * @param report {@link ClosedNegligenceReport} to save
     */
    void takenAction(ClosedNegligenceReport report);

    /**
     * Retrieve all {@link OpenNegligenceReport} owned to a {@link Negligent}.
     * @param negligent negligent of report
     * @return the {@link List} of all reports
     */
    List<OpenNegligenceReport> retrieveOpenReportsForNegligent(Negligent negligent);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Negligent}.
     * @param negligent negligent of report
     * @return the {@link List} of all reports
     */
    List<ClosedNegligenceReport> retrieveClosedReportsForNegligent(Negligent negligent);

    /**
     * Retrieve all {@link OpenNegligenceReport} assigned to a {@link Assignee}.
     * @param assignee assignee of report
     * @return the {@link List} of all reports
     */
    List<OpenNegligenceReport> retrieveOpenReportsForAssignee(Assignee assignee);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Assignee}.
     * @param assignee assignee of report
     * @return the {@link List} of all reports
     */
    List<ClosedNegligenceReport> retrieveClosedReportsForAssignee(Assignee assignee);

}
