/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.NegligenceSolution;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;

import java.util.List;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService {

    /**
     * Retrieve all {@link OpenNegligenceReport} assigned to a {@link Maintainer}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Maintainer}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(String username);

    /**
     * Takes action against a {@link Courier} providing solution.
     * @param report report to close
     * @param solution {@link NegligenceSolution} used to take action
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(OpenNegligenceReport report, NegligenceSolution solution);

    /**
     * Subscribes to {@link NewNegligence} events.
     * @param courier {@link Courier} username to receive {@link NewNegligence} from
     */
    void subscribeToCourierNegligence(String courier);
}
