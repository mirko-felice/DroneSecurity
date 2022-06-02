/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.NegligenceActionForm;

import java.util.List;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService extends SubscribableNegligenceReportService {

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
     * Takes action against a {@link Courier} using a {@link NegligenceActionForm}.
     * @param form the form to use
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(NegligenceActionForm form);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
