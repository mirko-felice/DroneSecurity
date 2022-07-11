/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.services;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;

import java.util.List;

/**
 * Domain Service dedicated to {@link Negligent} needs, related to negligence reporting.
 */
public interface NegligentReportsManager {

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

}
