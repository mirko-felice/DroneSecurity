/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;

import java.util.List;

/**
 * Service dedicated to {@link Courier} needs, related to negligence reporting.
 */
public interface CourierNegligenceReportService extends SubscribableNegligenceReportService {

    /**
     * Retrieve all {@link OpenNegligenceReport} owned to a {@link Courier}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Courier}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(String username);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static CourierNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
