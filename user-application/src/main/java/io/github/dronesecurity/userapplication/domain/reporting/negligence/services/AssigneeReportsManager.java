/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.services;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;

import java.util.List;

/**
 * Domain Service dedicated to {@link Assignee} needs, related to negligence reporting.
 */
public interface AssigneeReportsManager {

    /**
     * Takes action against a {@link Negligent} providing solution.
     * @param report {@link OpenNegligenceReport} to close
     * @param actionForm {@link NegligenceActionForm} used to take action
     */
    void takeAction(OpenNegligenceReport report, NegligenceActionForm actionForm);

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
