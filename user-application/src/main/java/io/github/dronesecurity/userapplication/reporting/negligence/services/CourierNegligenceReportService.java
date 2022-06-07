/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service dedicated to {@link Courier} needs, related to negligence reporting.
 */
public interface CourierNegligenceReportService {

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
     * Subscribe to {@link NewNegligence} events.
     * @param consumer {@link Consumer} to execute when a {@link NewNegligence} is raised
     */
    void subscribeToNewNegligence(Consumer<NewNegligence> consumer);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static CourierNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
